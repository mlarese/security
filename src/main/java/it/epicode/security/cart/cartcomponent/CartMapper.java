package it.epicode.security.cart.cartcomponent;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    private final ModelMapper modelMapper;

    public CartMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CartDTO toDto(Cart cart) {
        return modelMapper.map(cart, CartDTO.class);
    }

    public Cart toEntity(CartDTO cartDTO) {
        return modelMapper.map(cartDTO, Cart.class);
    }
}