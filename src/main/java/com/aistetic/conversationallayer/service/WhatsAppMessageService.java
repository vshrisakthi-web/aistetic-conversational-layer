package com.aistetic.conversationallayer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WhatsAppMessageService {

    @Value("${whatsapp.access-token}")
    private String accessToken;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    private final RestClient restClient =
            RestClient.create("https://graph.facebook.com");

    public void sendTextMessage(String recipientPhoneNumber, String message) {

        restClient.post()
                .uri("/v23.0/" + phoneNumberId + "/messages")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .body("""
                        {
                          "messaging_product": "whatsapp",
                          "to": "%s",
                          "type": "text",
                          "text": {
                            "body": "%s"
                          }
                        }
                        """.formatted(
                        recipientPhoneNumber,
                        message.replace("\"", "\\\"")
                ))
                .retrieve()
                .toBodilessEntity();
    }
}