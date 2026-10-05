package com.aistetic.conversationallayer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI conversationalLayerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aistetic Conversational Layer API")
                        .version("1.0.0")
                        .description(
                                "REST API for the Aistetic Conversational Layer " +
                                        "including conversations, listings, inventory, " +
                                        "health checks, and WhatsApp webhook integration."
                        ));
    }
}