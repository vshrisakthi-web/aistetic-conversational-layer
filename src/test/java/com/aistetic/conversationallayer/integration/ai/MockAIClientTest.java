package com.aistetic.conversationallayer.integration.ai;

import com.aistetic.conversationallayer.exception.AIServiceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class MockAIClientTest {

    @Test
    void analyzeImage_shouldThrowAIServiceException_whenAIShouldFail() {

        MockAIClient mockAIClient = new MockAIClient();

        assertThrows(
                AIServiceException.class,
                () -> mockAIClient.analyzeImage("https://example.com/fail-ai-image")
        );
    }
}