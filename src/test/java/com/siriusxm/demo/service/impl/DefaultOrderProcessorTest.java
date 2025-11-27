package com.siriusxm.demo.service.impl;


import com.siriusxm.demo.domain.ProcessingResult;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.service.MenuService;
import com.siriusxm.demo.service.imp.DefaultOrderProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DefaultOrderProcessorTest {

    @Autowired
    private DefaultOrderProcessor orderProcessor;

    @Autowired
    private MenuService menuService;

    @Test
    void testProcessOrder() {
        MenuItem icedLatte = menuService.getMenuItemByName("Iced Latte").orElseThrow();
        MenuItem greenTea = menuService.getMenuItemByName("Green Tea").orElseThrow();

        Order order = Order.builder()
                .id("123")
                .name("Test Order")
                .description("Test Description")
                .items(List.of(icedLatte, greenTea))
                .build();

        ProcessingResult result = orderProcessor.process(order);

        assertThat(result, is(notNullValue()));
        assertTrue(result.isSuccessful());
        assertThat(result.getMessages().size(), is(4)); // Validation, Inventory, Fee Calculation, Prepare Beverage
        assertThat(result.getTotalPrice(), is(new BigDecimal("7.75")));
    }
}