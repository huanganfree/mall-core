package com.ha.springbootdemo.modules.goods.controller;

import com.ha.springbootdemo.common.Result;
import com.ha.springbootdemo.modules.goods.service.SeckillService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/goods")
public class SeckillController {

    @Qualifier("redisMqSeckill")
    private final SeckillService seckillService;

    // 秒杀
    @PostMapping("doSeckill")
    public Result<String> doSeckill(@RequestParam("goodsId") Long goodsId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return seckillService.executeSeckill(userId, goodsId);
    }
}
