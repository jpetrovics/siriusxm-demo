package com.siriusxm.demo.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProcessingResult {
    private boolean successful;
    private final String orderId;
    private final LocalDateTime processedAt;
    private final List<String> messages;
    private BigDecimal totalPrice;

    public ProcessingResult(boolean successful, String orderId) {
        this.successful = successful;
        this.orderId = orderId;
        this.processedAt = LocalDateTime.now();
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
