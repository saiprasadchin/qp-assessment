package com.groceryapplication.controller;

import com.groceryapplication.dto.ProductRequest;
import com.groceryapplication.dto.ProductResponse;
import com.groceryapplication.service.ProductServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
public class GroceryController {

    private final ProductServiceImpl productService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String token) {
        productService.createProduct(productRequest, token);
     }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProduct();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void updateProduct(@RequestBody ProductRequest productRequest) {
        productService.updateProduct(productRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void removeProduct(@PathVariable Long productId) {
        productService.removeProduct(productId);
    }
}