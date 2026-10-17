package com.ha.springbootdemo.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 专门拦截 RuntimeException（运行时异常）
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        // e.getMessage() 就是你在 Service 里 throw "密码错误" 时的文字
        // 这里把它捕获后，包装成统一的 JSON 格式返回给前端
        return Result.fail(500, e.getMessage());
    }
}
