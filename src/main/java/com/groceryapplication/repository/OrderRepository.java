package com.groceryapplication.repository;

import com.groceryapplication.models.Order;
import com.groceryapplication.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
