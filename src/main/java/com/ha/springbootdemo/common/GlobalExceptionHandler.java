package com.ha.springbootdemo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 专门拦截 RuntimeException（运行时异常）
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error("发生运行时异常：", e);
        // e.getMessage() 就是你在 Service 里 throw "密码错误" 时的文字
        // 这里捕获后，包装成统一的 JSON 格式返回给前端
        // 自定义代码建议：一定要把堆栈打印出来，方便自己看后台！
        return Result.fail(500, e.getMessage());
    }
}
