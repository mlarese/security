package it.epicode.security.cart.cartcomponent;

import it.epicode.security.cart.product.Product;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "cart_items")
@Data
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long quantity;
    @ManyToOne
    private Product prodotto;

    @ManyToOne
    private Cart cart;
}
