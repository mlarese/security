package it.epicode.security.cart.cartcomponent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByUserName(String userName);
    Cart findByUserName(String userName);
}
