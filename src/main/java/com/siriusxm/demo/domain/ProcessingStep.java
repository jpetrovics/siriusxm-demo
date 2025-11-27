package com.siriusxm.demo.domain;

import com.siriusxm.demo.model.Order;

public interface ProcessingStep {
    StepResult execute(Order order);

    String getStepName();
}
