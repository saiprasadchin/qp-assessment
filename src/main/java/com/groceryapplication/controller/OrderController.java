package com.groceryapplication.controller;

import com.google.gson.Gson;
import com.groceryapplication.dto.OrderItemsDto;
import com.groceryapplication.dto.OrderRequest;
import com.groceryapplication.security.JWTGenerator;
import com.groceryapplication.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceImpl orderService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String token) {
        orderService.placeOrder(orderRequest, token);
        return "Order Placed Successfully";
    }

    //TODO : Fetch All User orders
}
