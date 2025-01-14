package it.epicode.security.cart.product;


import it.epicode.security.auth.AppUser;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String code;
    private String description;
    private Double price;
    // unità di misura
    private String um;
    private String category;

    @ManyToOne
    private AppUser seller;
}
