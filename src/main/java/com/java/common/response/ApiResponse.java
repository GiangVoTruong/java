package com.java.common.response;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Map<String, String> errors;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return success("Request processed successfully", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        response.timestamp = Instant.now();
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        return error(message, null);
    }

    public static <T> ApiResponse<T> error(String message, Map<String, String> errors) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.errors = errors;
        response.timestamp = Instant.now();
        return response;
    }
}
