package com.siriusxm.demo.controller;

import com.siriusxm.demo.domain.OrderProcessor;
import com.siriusxm.demo.domain.ProcessingResult;
import com.siriusxm.demo.exception.OrderProcessingException;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.model.OrderPriority;
import com.siriusxm.demo.model.OrderRequest;
import com.siriusxm.demo.service.MenuService;
import com.siriusxm.demo.service.imp.OrderProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderProcessorFactory processorFactory;
    private final MenuService menuService;

    @Autowired
    public OrderController(final OrderProcessorFactory processorFactory, final MenuService menuService) {
        this.processorFactory = processorFactory;
        this.menuService = menuService;
    }
    @PostMapping("/process")
    public ResponseEntity<ProcessingResult> processOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Received order request: {}", orderRequest);

        final List<MenuItem> menuItems = orderRequest.getItems().stream()
                .map(name -> menuService.getMenuItemByName(name)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Invalid menu item: " + name)))
                .collect(Collectors.toList());

        final Order order = Order.builder()
                .id(orderRequest.getId())
                .name(orderRequest.getName())
                .description(orderRequest.getDescription())
                .priority(OrderPriority.fromString(orderRequest.getPriority()))
                .items(menuItems)
                .build();

        log.info("Processing enriched order: {}", order);
        final OrderProcessor orderProcessor = processorFactory.getOrderProcessor(order)
                .orElseThrow(() -> new OrderProcessingException(order.getId(),"getOrderProcessor", "No order processor found"));
        final ProcessingResult result = orderProcessor.process(order);
        return result.isSuccessful() ? ResponseEntity.ok(result) : ResponseEntity.badRequest().body(result);
    }

}



