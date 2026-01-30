package com.example.demo.repository;

import com.example.demo.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByShoppingCartIdAndBookId(Long shoppingCartId, Long bookId);

    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);
}
