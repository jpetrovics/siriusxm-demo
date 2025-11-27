package com.siriusxm.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderProcessingException extends RuntimeException {
    private final String orderId;
    private final String stepName;

    public OrderProcessingException(final String orderId, final String stepName, String message) {
        super(String.format("Order %s failed at step %s: %s", orderId, stepName, message));
        this.orderId = orderId;
        this.stepName = stepName;
    }

    public OrderProcessingException(final String orderId, final String stepName, String message, Throwable cause) {
        super(String.format("Order %s failed at step %s: %s", orderId, stepName, message), cause);
        this.orderId = orderId;
        this.stepName = stepName;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStepName() {
        return stepName;
    }
}
