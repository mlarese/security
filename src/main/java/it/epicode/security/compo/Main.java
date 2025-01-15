package it.epicode.security.compo;

public class Main {
    public static void main(String[] args) {

        Dipendente d = new Dipendente();
        d.setNome("Mario");
        d.setPosizione("Dipendente");

        Leader l = new Leader();
        l.setNome("Luca");
        l.setPosizione("Leader");
        l.getSottoposti().add(d);

        Manager m = new Manager();
        m.setNome("Giovanni");
        m.setPosizione("Manager");
        m.getSottoposti().add(l);

        m.mostraDettagli();


    }
}
