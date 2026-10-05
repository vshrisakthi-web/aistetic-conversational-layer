package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.dto.ApiResponse;
import com.aistetic.conversationallayer.dto.HealthResponse;
import com.aistetic.conversationallayer.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(
        name = "Health",
        description = "Application health endpoints"
)
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @Operation(
            summary = "Check application health",
            description = "Returns the current health status of the application."
    )
    @GetMapping("/health")
    public ApiResponse<HealthResponse> health() {

        HealthResponse healthResponse = healthService.getHealthStatus();

        return new ApiResponse<>(
                true,
                "Request successful",
                null,
                healthResponse
        );
    }
}