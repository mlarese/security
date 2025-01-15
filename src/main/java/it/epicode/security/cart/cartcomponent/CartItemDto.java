package it.epicode.security.cart.cartcomponent;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long quantity;
    private Double price;

    private Long idProdotto;
    private Long idCart;
}
