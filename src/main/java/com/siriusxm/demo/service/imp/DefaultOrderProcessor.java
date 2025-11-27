package com.siriusxm.demo.service.imp;

import com.siriusxm.demo.domain.OrderProcessor;
import com.siriusxm.demo.domain.ProcessingResult;
import com.siriusxm.demo.domain.ProcessingStep;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.service.steps.FeeCalculationStep;
import com.siriusxm.demo.service.steps.InventoryCheckStep;
import com.siriusxm.demo.service.steps.PrepareBeverageStep;
import com.siriusxm.demo.service.steps.ValidationStep;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("defaultOrderProcessor")
public class DefaultOrderProcessor implements OrderProcessor {

    private final List<ProcessingStep> steps;

    @Autowired
    public DefaultOrderProcessor(final ValidationStep validationStep, final InventoryCheckStep inventoryCheckStep,
                                 final FeeCalculationStep feeCalculationStep, final PrepareBeverageStep prepareBeverageStep) {
        steps = List.of(validationStep, inventoryCheckStep, feeCalculationStep, prepareBeverageStep);
    }

    @Override
    public ProcessingResult process(final Order order) {

        ProcessingResult result = new ProcessingResult(true, order.getId());

        steps.forEach(step -> {
                log.info("Processing step: " + step.getStepName());
                var stepResult = step.execute(order);
                result.addMessage(stepResult.message());
                if(!stepResult.successful()) {
                    log.info("Order processing failed at {}", step.getStepName());
                    result.setSuccessful(false);
                }
            });

        result.setTotalPrice(order.getSubtotal().add(order.getFee()));
        log.info("Order processed successfully");
        return result;
    }
}
