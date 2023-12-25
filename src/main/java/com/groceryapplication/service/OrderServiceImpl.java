package com.groceryapplication.service;

import com.groceryapplication.dto.OrderItemsDto;
import com.groceryapplication.dto.OrderRequest;
import com.groceryapplication.models.Order;
import com.groceryapplication.models.OrderItems;
import com.groceryapplication.models.Product;
import com.groceryapplication.models.user.User;
import com.groceryapplication.repository.OrderRepository;
import com.groceryapplication.repository.UserRepository;
import com.groceryapplication.security.JWTGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final ProductServiceImpl productService;
    private final JWTGenerator jwtGenerator;
    private final UserRepository userRepository;
    public void placeOrder(OrderRequest orderRequest, String token) {
        Order order = new Order();
       // order.setUserId(1234L); //Todo add user code
        System.out.println(orderRequest.toString());
        String username = jwtGenerator.getUsernameFromToken(token);

        Optional<User> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            //TODO : Throw exception
        }

        order.setUserId(user.get().getUserId());

        List<OrderItems> orderItems = orderRequest.getOrderItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderItems(orderItems);

        //TODO : Call Inventory Service, and place order if product is in

        List<Long> productIds = orderItems.stream().map(OrderItems::getProductId).toList();

        List<Product> allOrderedProducts = productService.getAllProductsByIds(productIds);

        for(OrderItems item : orderItems) {
            Optional<Product> product1 = allOrderedProducts.stream()
                    .filter(product -> product.getProductId().equals(item.getProductId()))
                    .findFirst();

            if(product1.isPresent()){
                if (product1.get().getQuantity() < item.getQuantity()){
                    throw new IllegalArgumentException("Product is not in stock, please try again later");
                }
            }
        }

        updateProductQuantity(orderItems, allOrderedProducts);
        orderRepository.save(order);
    }

    private void updateProductQuantity(List<OrderItems> orderItems, List<Product> allOrderedProducts) {
        List<Product> products = new ArrayList<>();
        for(OrderItems item : orderItems) {
            Optional<Product> product1 = allOrderedProducts.stream()
                    .filter(product -> product.getProductId().equals(item.getProductId()))
                    .findFirst();

            if (product1.isPresent()) {
                Product product = product1.get();
                product.setQuantity(product.getQuantity() - item.getQuantity());
                products.add(product);
            }
        }

        productService.updateProductQuantity(products);
    }
    private OrderItems mapToDto(OrderItemsDto orderItemsDto) {
        OrderItems orderLineItems = new OrderItems();
        orderLineItems.setPrice(orderItemsDto.getPrice());
        orderLineItems.setProductId(orderItemsDto.getId());
        orderLineItems.setQuantity(orderItemsDto.getQuantity());
        return orderLineItems;
    }
}
