package it.epicode.security.ada;

import lombok.Data;

@Data
public class UtenteNuovo implements IUtenteNuovo {
    private String firstName;
    private String secondName;
}
