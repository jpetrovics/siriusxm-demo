package com.siriusxm.demo.exception;

public class InventoryException extends OrderProcessingException {
    public InventoryException(String orderId, String message) {
        super(orderId, "INVENTORY_CHECK", message);
    }
}
