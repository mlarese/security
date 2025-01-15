package it.epicode.security.chain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pagamento {
    private String descrizione;
    private double importo;
    private double limiteSpesa;
    private double disponibilità;

}
