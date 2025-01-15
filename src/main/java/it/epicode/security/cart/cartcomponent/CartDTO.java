package it.epicode.security.cart.cartcomponent;

import it.epicode.security.auth.AppUser;

import java.util.List;

public class CartDTO {
    private Long id;
    private List<CartItem> items;
    private AppUser user;
}