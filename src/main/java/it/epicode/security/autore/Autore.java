package it.epicode.security.autore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "autori")
@Data
public class Autore {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;
    String nome;
    String cognome;
    String nazionalita;
}
