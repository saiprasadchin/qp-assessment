package com.groceryapplication.repository;

import com.groceryapplication.models.Product;
//import com.groceryapplication.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductIdIn(List<Long> ids);
}
