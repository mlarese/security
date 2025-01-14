package it.epicode.security.cart.cartcomponent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    CartItemInsertResponse findCartItemById(Long id);
}
