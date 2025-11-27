package com.siriusxm.demo.controller;

import com.siriusxm.demo.domain.ProcessingResult;
import com.siriusxm.demo.model.MenuItem;
import com.siriusxm.demo.model.Order;
import com.siriusxm.demo.model.OrderRequest;
import com.siriusxm.demo.service.MenuService;
import com.siriusxm.demo.service.imp.DefaultOrderProcessor;
import com.siriusxm.demo.service.imp.OrderProcessorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController controller;

    @Mock
    private OrderProcessorFactory orderProcessorFactory;

    @Mock
    private MenuService menuService;

    @Mock
    private DefaultOrderProcessor defaultOrderProcessor;

    @Test
    void testProcessOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setId("req1");
        orderRequest.setName("Test Order");
        orderRequest.setDescription("Sample order for testing");
        orderRequest.setPriority("STD");
        orderRequest.setItems(List.of("Iced Latte", "Green Tea"));

        MenuItem icedLatte = MenuItem.builder().id("101").name("Iced Latte").build();
        MenuItem greenTea = MenuItem.builder().id("102").name("Green Tea").build();

        when(menuService.getMenuItemByName("Iced Latte")).thenReturn(Optional.of(icedLatte));
        when(menuService.getMenuItemByName("Green Tea")).thenReturn(Optional.of(greenTea));

        ProcessingResult successResult = new ProcessingResult(true, "req1");
        successResult.setTotalPrice(new BigDecimal("6.25"));

        when(orderProcessorFactory.getOrderProcessor(any(Order.class))).thenReturn(Optional.of(defaultOrderProcessor));
        when(defaultOrderProcessor.process(any(Order.class))).thenReturn(successResult);

        var response = controller.processOrder(orderRequest);
        assertThat(response.getStatusCode().value(), is(200));

        // Verify that the orderProcessor was called with the correctly built Order object
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(defaultOrderProcessor).process(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();

        assertThat(capturedOrder.getId(), is("req1"));
        assertThat(capturedOrder.getName(), is("Test Order"));
        assertThat(capturedOrder.getDescription(), is("Sample order for testing"));
        assertThat(capturedOrder.getItems().size(), is(2));
        assertThat(capturedOrder.getItems().get(0).getName(), is("Iced Latte"));
        assertThat(capturedOrder.getItems().get(1).getName(), is("Green Tea"));
    }
}