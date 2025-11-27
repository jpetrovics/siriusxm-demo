package com.siriusxm.demo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Ingredient {
    private String id;
    private String name;
    private BigDecimal unitPrice;
    private int stock;

    public Ingredient() {
    }

    public Ingredient(String id, String name, BigDecimal unitPrice, int stock) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }


}
