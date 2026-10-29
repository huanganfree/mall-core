package com.ha.springbootdemo.consumer;

import com.ha.springbootdemo.dto.SeckillMessageDTO;
import com.ha.springbootdemo.modules.goods.entity.Order;
import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import com.ha.springbootdemo.modules.goods.mapper.OrderMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
class SeckillQueueConsumer {

    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    // 核心注解：死死盯住 "seckill.queue" 队列，一旦里面有消息，立刻抓过来执行
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "seckill.queue", arguments = {
                    // 核心配置 A：指定死信交换机（当消息变成死信后投递到哪里）
                    @Argument(name = "x-dead-letter-exchange", value = "seckill.dead.exchange"),
                    // 核心配置 B：指定死信路由键（以什么 RoutingKey 路由到死信队列）
                    @Argument(name = "x-dead-letter-routing-key", value = "seckill.dead.routing.key")
            }),
            exchange = @Exchange(name = "seckill.normal.exchange", type = "direct"),
            key = "seckill.routing.key"
    ))
    @Transactional(rollbackFor = Exception.class) // 数据库事务回到了这里！
    public void handleSeckillMessage(String jsonMessage, Channel channel, Message messageI) throws IOException {

        // 获取这条消息在当前通道的唯一标识（标签）
        long deliveryTag = messageI.getMessageProperties().getDeliveryTag();

        // 1. 增加非空校验，防止 Jackson 报 end-of-input 错误
        if (jsonMessage == null || jsonMessage.trim().isEmpty()) {
            System.err.println("收到空消息，直接签收/丢弃");
            // ⚠️ 核心：手动签收，告诉 RabbitMQ 从队列中删除此消息
//            channel.basicAck(deliveryTag, false);
            channel.basicNack(deliveryTag, false, false);
            return;
        }
//        还有要处理不是正经json对象的数据，所以用
        try {
            // 核心：手动将 JSON 字符串还原成 Java 对象（对标 JS 的 JSON.parse(jsonMessage)）
            SeckillMessageDTO message = objectMapper.readValue(jsonMessage, SeckillMessageDTO.class);

            Long goodsId = message.getGoodsId();
            Long userId = message.getUserId();
            System.out.println("【MQ后台消费者】接收到排队消息，开始为用户 " + userId + " 真正落库...");

            // 1. 扣减 MySQL 库存（依然利用悲观/乐观锁最后兜底，防止万一）
            int rows = goodsMapper.reduceGoods(goodsId);
            if (rows == 0) {
                // 如果扣减失败，说明数据库层发现真的没货了，直接抛弃或记录异常日志
                System.err.println("【落库失败】商品真的卖完了或数据异常，userId: " + userId);
                channel.basicNack(deliveryTag, false, false);
                return;
            }
            // 生成订单
            Order order = new Order();
            order.setGoodsId(goodsId);
            order.setUserId(userId);
            orderMapper.generateOrder(order);
            System.out.println("【落库成功】用户 " + userId + " 的订单已被成功写入数据库！");
        } catch (Exception e) {
            // 4. 捕获 Jackson 的所有解析异常（包含 MismatchedInputException 等）
            System.err.println("异常信息: " + e.getMessage());
            // ⚠️ 极其重要：遇到脏数据，绝对不能让它重回队列（requeue=true），否则会陷入无限报错死循环！
            // 方案 A（手动Ack模式）：直接单条签收（丢弃），让队列放行
            channel.basicNack(deliveryTag, false, false);
        }

    }


    /**
     * 死信队列监听（相当于垃圾分类后的处理厂）
     * 生产环境下，这里通常用于：打印告警日志、记录数据库以供后续人工排查
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "seckill.dead.queue"), // 死信队列名
            exchange = @Exchange(name = "seckill.dead.exchange", type = "direct"), // 死信交换机
            key = "seckill.dead.routing.key" // 对应上方绑定的死信 RoutingKey
    ))
    public void receiveDeadLetter(@Payload String deadMessage, Channel channel, Message message) throws Exception {
        System.err.println("【死信队列】这是一条死信/脏数据，内容是: " + deadMessage);

        // 记得死信队列的消息也要手动 Ack 签收掉，否则死信队列也会堆积 Unacked
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
