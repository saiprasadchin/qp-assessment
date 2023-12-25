package com.groceryapplication.service;

import com.groceryapplication.dto.ProductRequest;
import com.groceryapplication.dto.ProductResponse;
import com.groceryapplication.models.Product;
import com.groceryapplication.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl {
    private final ProductRepository productRepository;
    public void createProduct(ProductRequest productRequest, String token) {

        Product product = Product.builder()
                .productName(productRequest.getName())
                .description(productRequest.getDescription())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getProductId());
    }

    public void updateProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .productId(productRequest.getProductId())
                .productName(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());

        if (optionalProduct.isPresent()) {
            product.setQuantity(optionalProduct.get().getQuantity() + productRequest.getQuantity());
            productRepository.save(product);
            log.info("Product {} is saved", product.getProductId());
        } else {
            //TODO: Throw Exception
        }
    }

    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }
    public void removeProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    public List<Product> getAllProductsByIds(List<Long> productIds) {
        return productRepository.findByProductIdIn(productIds);
    }
    private ProductResponse mapToProductResponse( Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getProductName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .build();
    }
    public void updateProductQuantity(List<Product> products) {
        productRepository.saveAll(products);
    }

}
