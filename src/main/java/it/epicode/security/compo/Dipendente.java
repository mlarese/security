package it.epicode.security.compo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Dipendente {
    private String nome;
    private String posizione;
    private List<Dipendente> sottoposti=new ArrayList<>();

    public void mostraDettagli() {
        System.out.println("Nome: " + nome);
        System.out.println("Posizione: " + posizione);
        System.out.println("Sottoposti: ");
        for (Dipendente sottoposto : sottoposti) {
            sottoposto.mostraDettagli();
        }
    }
}
