package it.epicode.security.chain;

public class RevisoreDisponibilita extends Revisore {


    @Override
    public void verifica(Pagamento pagamento) {
        if (pagamento.getImporto() > pagamento.getDisponibilità()) {
            System.out.println("[X] Pagamento non autorizzato: fondi insufficienti");
            return;
        }
        if ( getSuccessivo()!= null) {
            System.out.println(" [V] Fondi sufficienti");
            getSuccessivo().verifica(pagamento);
        }
    }

}
