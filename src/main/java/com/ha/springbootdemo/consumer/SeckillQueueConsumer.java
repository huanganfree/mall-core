package com.ha.springbootdemo.consumer;

import com.ha.springbootdemo.dto.SeckillMessageDTO;
import com.ha.springbootdemo.modules.goods.entity.Order;
import com.ha.springbootdemo.modules.goods.mapper.GoodsMapper;
import com.ha.springbootdemo.modules.goods.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SeckillQueueConsumer {

    private final GoodsMapper goodsMapper;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    // 核心注解：死死盯住 "seckill.queue" 队列，一旦里面有消息，立刻抓过来执行
    @RabbitListener(queues = "seckill.queue")
    @Transactional(rollbackFor = Exception.class) // 数据库事务回到了这里！
    public void handleSeckillMessage(String jsonMessage) {

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
            return;
        }
        // 生成订单
        Order order = new Order();
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        orderMapper.generateOrder(order);
        System.out.println("【落库成功】用户 " + userId + " 的订单已被成功写入数据库！");
    }
}
