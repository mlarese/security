package it.epicode.security.libri;

import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroService {
    private final AppUserRepository appUserRepository;

    public List<String> findByUtente(String utente) {
        // cerco nel db lo user tramite user name

        AppUser appUser = appUserRepository.findByUsername(utente)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        return List.of("Il signore degli anelli", "Il trono di spade", "Il nome della rosa");
    }

}
