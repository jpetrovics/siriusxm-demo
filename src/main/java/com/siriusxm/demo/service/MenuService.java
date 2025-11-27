package com.siriusxm.demo.service;

import com.siriusxm.demo.model.*;
import com.siriusxm.demo.model.MenuItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final Map<String, Ingredient> ingredients = new HashMap<>();
    private final Map<String, Recipe> recipes = new HashMap<>();
    private final Map<String, MenuItem> menuItemsByName = new HashMap<>();
    // Initialize Ingredients
    private final Ingredient coffeeBeans = new Ingredient("1", "Coffee Beans", new BigDecimal("0.50"), 100);
    private final Ingredient hotWater = new Ingredient("2", "Hot Water", new BigDecimal("0.10"), Integer.MAX_VALUE);
    private final Ingredient milk = new Ingredient("3", "Milk", new BigDecimal("0.30"), 50);
    private final Ingredient sugar = new Ingredient("4", "Sugar", new BigDecimal("0.05"), 200);
    private final Ingredient ice = new Ingredient("5", "Ice", new BigDecimal("0.02"), Integer.MAX_VALUE);
    private final Ingredient teaLeaves = new Ingredient("6", "Tea Leaves", new BigDecimal("0.40"), 80);

    {
        ingredients.put(coffeeBeans.getId(), coffeeBeans);
        ingredients.put(hotWater.getId(), hotWater);
        ingredients.put(milk.getId(), milk);
        ingredients.put(sugar.getId(), sugar);
        ingredients.put(ice.getId(), ice);
        ingredients.put(teaLeaves.getId(), teaLeaves);
    }

    {
        // Initialize Recipes
        // Iced Latte Recipe
        Queue<RecipeStep> icedLatteSteps = new LinkedList<>();
        icedLatteSteps.add(new RecipeStep("Brew espresso", 1, coffeeBeans.getId()));
        icedLatteSteps.add(new RecipeStep("Add milk", 2, milk.getId()));
        icedLatteSteps.add(new RecipeStep("Add ice", 3, ice.getId()));
        icedLatteSteps.add(new RecipeStep("Stir", 4, null));
        icedLatteSteps.add(new RecipeStep("Mix", 5, null));

        Recipe icedLatteRecipe = Recipe.builder()
                .ingredients(List.of(coffeeBeans, milk, ice))
                .steps(icedLatteSteps)
                .price(new BigDecimal("3.50"))
                .build();
        recipes.put("iced_latte_recipe", icedLatteRecipe);

        // Green Tea Recipe
        Queue<RecipeStep> greenTeaSteps = new LinkedList<>();
        greenTeaSteps.add(new RecipeStep("Heat water", 1, hotWater.getId()));
        greenTeaSteps.add(new RecipeStep("Steep tea leaves", 2, teaLeaves.getId()));
        greenTeaSteps.add(new RecipeStep("Serve hot", 3, null));

        Recipe greenTeaRecipe = Recipe.builder()
                .ingredients(List.of(hotWater, teaLeaves))
                .steps(greenTeaSteps)
                .price(new BigDecimal("2.75"))
                .build();
        recipes.put("green_tea_recipe", greenTeaRecipe);
    }

    {
        // Initialize Menu Items
        MenuItem icedLatte = MenuItem.builder()
                .id("101")
                .type(BeverageType.COLD_COFFEE)
                .name("Iced Latte")
                .description("Refreshing cold coffee with milk")
                .price(new BigDecimal("3.50"))
                .recipeId("iced_latte_recipe")
                .build();

        MenuItem greenTea = MenuItem.builder()
                .id("102")
                .type(BeverageType.TEA)
                .name("Green Tea")
                .description("Hot and healthy green tea")
                .price(new BigDecimal("2.75"))
                .recipeId("green_tea_recipe")
                .build();

        menuItemsByName.put(icedLatte.getName(), icedLatte);
        menuItemsByName.put(greenTea.getName(), greenTea);
    }

    public Optional<MenuItem> getMenuItem(String id) {
        return menuItemsByName.values().stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    public Optional<MenuItem> getMenuItemByName(String name) {
        return Optional.ofNullable(menuItemsByName.get(name));
    }

    public Optional<Recipe> getRecipe(String recipeId) {
        return Optional.ofNullable(recipes.get(recipeId));
    }

    public List<Ingredient> getIngredientsForRecipe(String recipeId) {
        return recipes.getOrDefault(recipeId, Recipe.builder().build()).getIngredients();
    }

    public Queue<RecipeStep> getRecipeStepsForRecipe(String recipeId) {
        Recipe recipe = recipes.get(recipeId);
        if (recipe != null && recipe.getSteps() != null) {
            return new LinkedList<>(recipe.getSteps()); // Return a copy to avoid modification
        }
        return new LinkedList<>();
    }
}
