package it.epicode.security.cart.cartcomponent;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartItemMapper {
    private final ModelMapper modelMapper;
    public CartItemMapper() {
        modelMapper = new ModelMapper();
    }
    public CartItemDto toDto(CartItem cartItem) {
        CartItemDto cartItemDTO = modelMapper.map(cartItem, CartItemDto.class);
        cartItemDTO.setIdProdotto(cartItem.getProdotto().getId());
        cartItemDTO.setIdCart(cartItem.getCart().getId());
        return cartItemDTO;
    }

    public List<CartItemDto> toDtoList(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


}
