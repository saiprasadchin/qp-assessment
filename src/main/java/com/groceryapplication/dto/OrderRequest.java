package com.groceryapplication.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderRequest {
    private List<OrderItemsDto> orderItemsDtoList;
}