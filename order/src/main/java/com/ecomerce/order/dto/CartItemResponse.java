package com.ecomerce.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private String user;
    private String product;
    private Integer quantity;
    private BigDecimal price;
}
