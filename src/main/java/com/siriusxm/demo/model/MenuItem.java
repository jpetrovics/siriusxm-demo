package com.siriusxm.demo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MenuItem {
    private String id;
    private String name;
    private BeverageType type;
    private String description;
    private BigDecimal price;
    private String recipeId;
}
