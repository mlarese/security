package it.epicode.security.cart.cartcomponent;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartItemMapperDto {
    private final ModelMapper modelMapper;

    public CartItemMapperDto() {
        modelMapper = new ModelMapper();
    }

    public CartItemDto toDto(CartItem cartItem){
        CartItemDto cartItemdto = modelMapper.map(cartItem, CartItemDto.class);
        cartItemdto.setIdProdotto(cartItem.getProdotto().getId());
        cartItemdto.setIdCart(cartItem.getCart().getId());

        return cartItemdto;
    }

    public List<CartItemDto> toDtoList(List<CartItem> list) {
        return list.stream()
                .map(this::toDto)
                .toList()  ;
    }

}
