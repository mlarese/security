package it.epicode.security.cart.cartcomponent;

import it.epicode.security.cart.product.Product;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemInsertRequest {
    @Min(value = 1, message = "quantity must be greater than 0")
    Long quantity;
    @Min(value = 1, message = "price must be greater than 0")
    Double price;
    @NotNull(message = "idProdotto is mandatory")
    Long idProdotto;
}
