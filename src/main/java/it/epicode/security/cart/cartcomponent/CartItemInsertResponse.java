package it.epicode.security.cart.cartcomponent;

public interface CartItemInsertResponse {
    String getId();
    Integer getQuantity();
    Integer getPrice();
    String getProdottoDescription();
    Product getProdotto();
    interface Product {
        String getDescription();
    }
}
