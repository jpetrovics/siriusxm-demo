package com.siriusxm.demo.domain;

import com.siriusxm.demo.model.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessor {
    ProcessingResult process(Order order);
}
