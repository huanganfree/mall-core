package com.ha.springbootdemo.modules.goods.service.impl;

import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.dto.SeckillMessageDTO;
import com.ha.springbootdemo.modules.goods.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service("redisMqSeckill")
@RequiredArgsConstructor
public class RedisMqSeckillServiceImpl implements SeckillService {

    private final RabbitTemplate rabbitTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<String> executeSeckill(Long userId, Long goodsId) {
        // 访问redis
        Long leftStock = stringRedisTemplate.opsForValue().decrement("stock-" + goodsId);
        if (leftStock < 0) {
            stringRedisTemplate.opsForValue().increment("stock-" + goodsId);
            return Result.success("已售罄");
        }

        SeckillMessageDTO seckillMessageDTO = new SeckillMessageDTO();
        seckillMessageDTO.setGoodsId(goodsId);
        seckillMessageDTO.setUserId(userId);

        // 核心：手动将对象揉成普通的 JSON 字符串（对标 JS 的 JSON.stringify(dto)）
        String jsonMessage = objectMapper.writeValueAsString(seckillMessageDTO);

        rabbitTemplate.convertAndSend("seckill.queue", jsonMessage);
        return Result.success("排队中");
    }
}
