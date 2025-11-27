package com.siriusxm.demo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Queue;

@Data
@Builder
public class Recipe {
    private List<Ingredient> ingredients;
    private Queue<RecipeStep> steps;
    private BigDecimal price;
}
