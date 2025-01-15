package it.epicode.security.chain;

public class RevisoreLimiteDiSpesa extends Revisore{
    @Override
    void verifica(Pagamento pagamento) {
        if(pagamento.getImporto()>pagamento.getLimiteSpesa()){
            System.out.println("[X] Pagamento non autorizzato: limite di spesa superato");
            return;
        }
        System.out.println("[V] Limite di spesa non superato pagamento autorizzato");

        if(getSuccessivo()!=null){
            getSuccessivo().verifica(pagamento);
        }

    }
}
