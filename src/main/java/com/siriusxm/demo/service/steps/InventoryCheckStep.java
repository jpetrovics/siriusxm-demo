package com.siriusxm.demo.service.steps;

import com.siriusxm.demo.domain.ProcessingStep;
import com.siriusxm.demo.domain.StepResult;
import com.siriusxm.demo.exception.InventoryException;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InventoryCheckStep implements ProcessingStep {

    @Autowired
    private MenuService menuService;

    @Override
    public StepResult execute(final Order order) {
        try {
            List<MenuItem> unavailableItems = checkInventory(order.getItems());
            if (!unavailableItems.isEmpty()) {
                String message = "Items not available: " + unavailableItems.stream()
                        .map(MenuItem::getName)
                        .collect(Collectors.joining(", "));
                return StepResult.failure(getStepName(), message);
            }

            return StepResult.success(getStepName(), "Inventory check passed");
        } catch (Exception e) {
            log.error("Inventory check failed: " + e.getMessage());
            throw new InventoryException(order.getId(), "Inventory check failed: " + e.getMessage());
        }
    }

    private List<MenuItem> checkInventory(final List<MenuItem> items) {
        return items.stream()
                .filter(item -> menuService.getMenuItem(item.getId()).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public String getStepName() {
        return "Inventory Check";
    }
}
