package com.siriusxm.demo.service.steps;

import com.siriusxm.demo.domain.ProcessingStep;
import com.siriusxm.demo.domain.StepResult;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FeeCalculationStep implements ProcessingStep {

    private static final BigDecimal STANDARD_FEE = new BigDecimal("1.50");
    private static final BigDecimal HIGH_PRIORITY_MULTIPLIER = new BigDecimal("2.5");
    private final MenuService menuService;

    @Autowired
    public FeeCalculationStep(final MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public StepResult execute(final Order order) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (MenuItem item : order.getItems()) {
            subtotal = subtotal.add(menuService.getMenuItem(item.getId())
                    .map(MenuItem::getPrice)
                    .orElse(BigDecimal.ZERO));
        }
        order.setSubtotal(subtotal);

        BigDecimal fee = STANDARD_FEE;
        if (order.getPriority() == com.siriusxm.demo.model.OrderPriority.HIGH) {
            fee = fee.multiply(HIGH_PRIORITY_MULTIPLIER);
        }
        order.setFee(fee);

        return StepResult.success(getStepName(), "Subtotal calculated: " + subtotal + ", Fee applied: " + fee);
    }

    @Override
    public String getStepName() {
        return "Fee Calculation";
    }
}