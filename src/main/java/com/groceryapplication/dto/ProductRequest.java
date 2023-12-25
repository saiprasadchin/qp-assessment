package com.groceryapplication.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductRequest {
    private Long productId;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
}
