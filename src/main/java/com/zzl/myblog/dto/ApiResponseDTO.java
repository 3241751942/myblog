package com.zzl.myblog.dto;

import lombok.Data;

/**
 * 统一API响应格式
 */
@Data
public class ApiResponseDTO<T> {

    /**
     * 状态码：200成功，其他失败
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    private ApiResponseDTO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponseDTO<T> success() {
        return new ApiResponseDTO<>(200, "success", null);
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponseDTO<T> success(T data) {
        return new ApiResponseDTO<>(200, "success", data);
    }

    /**
     * 成功响应（自定义消息）
     */
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(200, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(500, message, null);
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> ApiResponseDTO<T> error(Integer code, String message) {
        return new ApiResponseDTO<>(code, message, null);
    }
}