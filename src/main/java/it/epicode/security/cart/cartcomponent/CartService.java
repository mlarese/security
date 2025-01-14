package it.epicode.security.cart.cartcomponent;

import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final AppUserService appUserService;

    // salva un cart recuperando lo user da username
    public Cart saveCart(String userName) {
        Cart cart = new Cart();
        AppUser user = appUserService.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));

        cart.setUser(user);
        return cartRepository.save(cart);


    }

}
