package com.siriusxm.demo.service.steps;

import com.siriusxm.demo.domain.ProcessingStep;
import com.siriusxm.demo.domain.StepResult;
import com.siriusxm.demo.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ValidationStep implements ProcessingStep {
    @Override
    public StepResult execute(Order order) {
        if (order == null || CollectionUtils.isEmpty(order.getItems())) {
            return StepResult.failure(getStepName(), "Order or items cannot be empty");
        }
        return StepResult.success(getStepName(), "Validation passed");
    }

    @Override
    public String getStepName() {
        return "Validation";
    }
}
