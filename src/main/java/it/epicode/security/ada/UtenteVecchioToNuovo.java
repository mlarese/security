package it.epicode.security.ada;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UtenteVecchioToNuovo implements IUtenteNuovo{
   UtenteVecchio utenteVecchio;

    @Override
    public String getFirstName() {
        return utenteVecchio.getNome();
    }

    @Override
    public String getSecondName() {
        return utenteVecchio.getCognome();
    }


}
