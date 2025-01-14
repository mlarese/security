package it.epicode.security.cart.cartcomponent;

import it.epicode.security.auth.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class
Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items = new ArrayList<>();

    @OneToOne
    private AppUser user;
}
