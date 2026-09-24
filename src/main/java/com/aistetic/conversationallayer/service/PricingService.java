package com.aistetic.conversationallayer.service;
import org.springframework.stereotype.Service;
import com.aistetic.conversationallayer.dto.ProductAnalysis;

@Service
public class PricingService {
    public Integer suggestPrice(ProductAnalysis productAnalysis) {
        //mock
        if ("Nike".equalsIgnoreCase(productAnalysis.getBrand())
                && "Good".equalsIgnoreCase(productAnalysis.getCondition())) {
            return 3999;
        }

        return 1999;
    }
}
