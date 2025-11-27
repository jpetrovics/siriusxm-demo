package com.siriusxm.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String id;
    private String name;
    private String description;
    private List<String> items;
    private String priority;
}
