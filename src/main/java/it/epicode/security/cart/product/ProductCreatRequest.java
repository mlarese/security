package it.epicode.security.cart.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCreatRequest {
    @NotBlank(message = "code is mandatory")
    private String code;
    @NotBlank(message = "description is mandatory")
    private String description;

    @Min(value = 0, message = "price must be greater than 0")
    private Double price;

    @NotBlank(message = "um is mandatory")
    private String um;
    @NotBlank(message = "category is mandatory")
    private String category;
}
