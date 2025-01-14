package it.epicode.security.autore;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AutoreInsertDto {
    @NotBlank(message = "Il nome non può essere vuoto")
    String nome;
    @NotBlank(message = "Il cognome non può essere vuoto")
    String cognome;
    @NotBlank(message = "La nazionalità non può essere vuota")
    String nazionalita;
}
