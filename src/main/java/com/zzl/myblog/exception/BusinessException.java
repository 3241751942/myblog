package com.zzl.myblog.exception;

import lombok.Getter;
import lombok.Setter;

// 业务异常类
@Setter
@Getter
public class BusinessException extends RuntimeException {

    private Integer code;

    // 只传消息，默认错误码
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    // 传错误码和消息
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    // 传消息和 cause
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
    }

}