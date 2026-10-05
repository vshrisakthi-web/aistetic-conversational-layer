package com.aistetic.conversationallayer.dto;

public class ApiResponse<T> {

    private boolean success;
    private String message;
    private String errorCode;
    private T data;

    public ApiResponse(
            boolean success,
            String message,
            String errorCode,
            T data) {

        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public T getData() {
        return data;
    }
}