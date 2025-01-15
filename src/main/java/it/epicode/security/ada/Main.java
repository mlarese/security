package it.epicode.security.ada;

public class Main {
    public static void main(String[] args) {
        UtenteVecchio u = new UtenteVecchio();
        u.setNome("Mario");
        u.setCognome("Rossi");
        UtenteVecchioToNuovo u2 = new UtenteVecchioToNuovo(u);



        StampaUtility.stampa(u2);
    }
}
