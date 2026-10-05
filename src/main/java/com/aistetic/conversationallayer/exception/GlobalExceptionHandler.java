package com.aistetic.conversationallayer.exception;

import com.aistetic.conversationallayer.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---------------------------------------------------------
    // 1. Conversation Not Found
    // ---------------------------------------------------------

    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleConversationNotFound(
            ConversationNotFoundException exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                exception.getMessage(),
                "CONVERSATION_NOT_FOUND",
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    // ---------------------------------------------------------
    // 2. AI Service Error
    // ---------------------------------------------------------

    @ExceptionHandler(AIServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleAIServiceException(
            AIServiceException exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                exception.getMessage(),
                "AI_SERVICE_ERROR",
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(response);
    }


    // ---------------------------------------------------------
    // 3. Validation Error
    // ---------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            MethodArgumentNotValidException exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Invalid request",
                "INVALID_REQUEST",
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // ---------------------------------------------------------
    // 4. Illegal Argument
    // ---------------------------------------------------------

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                exception.getMessage(),
                "INVALID_REQUEST",
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    // ---------------------------------------------------------
    // 5. Unexpected Error
    // ---------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception exception) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Internal server error",
                "INTERNAL_SERVER_ERROR",
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}