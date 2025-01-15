package it.epicode.security.chain;

import lombok.Data;

@Data
public abstract class Revisore {
    Revisore successivo;
    abstract void  verifica(Pagamento pagamento);
}
