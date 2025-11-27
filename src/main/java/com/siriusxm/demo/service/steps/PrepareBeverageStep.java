package com.siriusxm.demo.service.steps;

import com.siriusxm.demo.domain.ProcessingStep;
import com.siriusxm.demo.domain.StepResult;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.model.Recipe;
import com.siriusxm.demo.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class PrepareBeverageStep implements ProcessingStep {

    @Autowired
    private MenuService menuService;

    @Override
    public StepResult execute(Order order) {
        for(MenuItem menuItem : order.getItems()) {
            log.info("Making beverage: " + menuItem.getName());
            Optional<Recipe> recipeOptional = menuService.getRecipe(menuItem.getRecipeId());

            if (recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                if (recipe.getSteps() != null) {
                    while (!recipe.getSteps().isEmpty()) {
                        log.info(recipe.getSteps().poll().step());
                    }
                }
            } else {
                log.warn("No recipe found for menu item: {}", menuItem.getName());
            }
        }
        return new StepResult(true, "Prepare Beverage", "Beverage prepared successfully");
    }

    @Override
    public String getStepName() {
        return "Prepare Beverage";
    }
}
