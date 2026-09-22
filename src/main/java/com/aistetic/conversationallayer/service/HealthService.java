package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.dto.HealthResponse;
import org.springframework.stereotype.Service;

@Service
public class HealthService {

    public HealthResponse getHealthStatus() {
        return new HealthResponse("UP");
    }
}