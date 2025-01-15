package it.epicode.security.chain;

public class Main {
    public static void main(String[] args) {
        Pagamento p= new Pagamento("Acquisto", 1000, 1500, 1500);
        RevisoreDisponibilita rd = new RevisoreDisponibilita();
        RevisoreLimiteDiSpesa rl = new RevisoreLimiteDiSpesa();

        rd.setSuccessivo(rl);

        rd.verifica(p);


    }
}
