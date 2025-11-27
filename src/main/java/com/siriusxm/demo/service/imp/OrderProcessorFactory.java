package com.siriusxm.demo.service.imp;

import com.siriusxm.demo.domain.OrderProcessor;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.model.OrderPriority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class OrderProcessorFactory {

    private final Map<String, OrderProcessor> orderProcessors;

    @Autowired
    public OrderProcessorFactory(final Map<String, OrderProcessor> orderProcessors) {
        this.orderProcessors = orderProcessors;
    }

    public Optional<OrderProcessor> getOrderProcessor(final Order order) {
        if (order.getPriority() == OrderPriority.HIGH) {
            return Optional.ofNullable(orderProcessors.get("highPriorityOrderProcessor"));
        } else {
            return Optional.ofNullable(orderProcessors.get("defaultOrderProcessor"));
        }
    }
}
