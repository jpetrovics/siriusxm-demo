package com.siriusxm.demo.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Order {
    private String id;
    private String name;
    private String description;
    private OrderPriority priority;
    private BigDecimal subtotal;
    private BigDecimal fee;
    @Builder.Default
    private List<MenuItem> items = new ArrayList<>();
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
