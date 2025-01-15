package it.epicode.security.cart.cartcomponent;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart-item")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItemDto> createCartItem(@RequestBody CartItemInsertRequest request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(cartItemService.saveCartItem(request, user.getUsername()));
    }

    @GetMapping
    public ResponseEntity<?> getItems() {
        return ResponseEntity.ok(cartItemService.getItems());
    }

}
