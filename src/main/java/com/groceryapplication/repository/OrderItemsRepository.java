package com.groceryapplication.repository;

import com.groceryapplication.models.Order;
import com.groceryapplication.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
}
