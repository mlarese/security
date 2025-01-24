package it.epicode.security.cart.cartcomponent;

import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserService;
import it.epicode.security.cart.product.Product;
import it.epicode.security.cart.product.ProductRepository;
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
    private final ProductRepository productRepository;

    public CartItemInsertResponse saveCartItem(CartItemInsertRequest request, String username ) {
        Optional<AppUser> user = appUserService.findByUsername(username);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        if(!cartRepository.existsByUserUsername(username)) {
            throw new EntityNotFoundException("Cart not found");
        }
        Cart cart = cartRepository.findByUserUsername(username);

        Product p = productRepository.findById(request.getIdProdotto()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(request, cartItem);

        cartItem.setCart(cart);
        cartItem.setProdotto(p);
        cart.getItems().add(cartItem);

        cartItemRepository.save(cartItem);
        return cartItemRepository.findCartItemById(cartItem.getId());
    }


}
