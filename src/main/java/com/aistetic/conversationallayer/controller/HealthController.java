package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.dto.ApiResponse;
import com.aistetic.conversationallayer.dto.HealthResponse;
import com.aistetic.conversationallayer.service.HealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public ApiResponse<HealthResponse> health() {

        HealthResponse healthResponse = healthService.getHealthStatus();

        return new ApiResponse<>(
                true,
                "Request successful",
                healthResponse
        );
    }
}