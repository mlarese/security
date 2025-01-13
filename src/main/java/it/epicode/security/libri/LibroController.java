package it.epicode.security.libri;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libri")
// tutti i metodi della classe di default seguono questa preautorizzazione
// posso sovrascrivere mettendo il PreAuthorize direttamente sopra il metodo
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
public class LibroController {
    private final LibroService libroService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getLibri() {
        return "Lista dei libri disponibile agli admin";
    }

    @GetMapping("/utente")
    @PreAuthorize("hasRole('USER')")
    public String getLibriUser() {
        return "Lista dei libri disponibile agli user";
    }

    @GetMapping("/tutti")
    @PreAuthorize("isAuthenticated()")
    public String getLibriAutenticato() {
        return "Lista dei libri disponibile a tutti";
    }

    @GetMapping("/ruoli_specifici")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String getLibriSpecifici() {
        return "Lista dei libri disponibile a ruoli specifici";
    }

    @GetMapping("/recupero_user")
    public String getLibriConRecuperoUser(@AuthenticationPrincipal User utente) {

        libroService.findByUtente(utente.getUsername());

        System.out.println(utente);
        return "Lista dei libri disponibile per tutti con lo user";
    }

}
