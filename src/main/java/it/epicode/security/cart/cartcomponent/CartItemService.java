package it.epicode.security.cart.cartcomponent;

import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final AppUserService appUserService;

    // ho usato il repository per questione di tempo
    // andrebbe sempre utilizzato un service che contiene già dentro di se la gestione
    // delle eccezioni

    private final CartRepository cartRepository;

    public CartItem saveCartItem(CartItemInsertRequest request, String username ) {
        Optional<AppUser> user = appUserService.findByUsername(username);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        if(!cartRepository.existsByUserName(username)) {
            throw new EntityNotFoundException("Cart not found");
        }
        Cart cart = cartRepository.findByUserName(username);

        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(request, cartItem);

        cartItem.setCart(cart);
        cart.getItems().add(cartItem);

        return cartItemRepository.save(cartItem);
    }


}
