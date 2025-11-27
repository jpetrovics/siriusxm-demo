package com.siriusxm.demo.model;

public enum OrderPriority { STANDARD("STD"), HIGH ("HIGH");

    private String name;

    OrderPriority(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public static OrderPriority fromString(String name) {
        for (var orderPriority : OrderPriority.values()) {
            if (orderPriority.name.equalsIgnoreCase(name)) {
                return orderPriority;
            }
        }
        throw new IllegalArgumentException("Unknown Order Priority type: " + name);
    }

}