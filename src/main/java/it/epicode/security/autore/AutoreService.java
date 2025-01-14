package it.epicode.security.autore;


import it.epicode.security.auth.AppUser;
import it.epicode.security.auth.AppUserRepository;
import it.epicode.security.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class AutoreService {
    private final AutoreRepo autoreRepo;
    private final AppUserRepository appUserRepository;

    public Page<Autore> findAll(Pageable pageable) {
        return autoreRepo.findAll(pageable);
    }

    // overload del saveAutore per fare il controllo del ruolo dentro il metodo del service
    public Autore saveAutore(@Valid AutoreInsertDto  autore, User user) {
        AppUser appUser = appUserRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + user.getUsername()));


        /// verifica se l'utente ha il ruolo ADMIN usando le GrantedAuthority
        // se non volete recuperare l'app user si puàò determinare l'appartenenza al ruolo ADMIN direttamente
        // dalla lista di Autority dell'utente di spring (org.springframework.security.core.userdetails.User;)
        /*
        boolean hasAdminRole = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // Ottiene la stringa del ruolo (ad esempio, "ROLE_ADMIN")
                .anyMatch("ROLE_ADMIN"::equals);

        boolean hasAdminRole = user
                .getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        */

        if(appUser.getRoles().stream()
                .map(Role::name)
                .noneMatch("ROLE_ADMIN"::equals))
            throw new SecurityException("Non hai i permessi per creare un autore");

        Autore autoreCreato = new Autore();
        BeanUtils.copyProperties(autore, autoreCreato);
        return autoreRepo.save(autoreCreato);
    }

    public Autore saveAutore(@Valid AutoreInsertDto  autore) {
        Autore autoreCreato = new Autore();
        BeanUtils.copyProperties(autore, autoreCreato);
        return autoreRepo.save(autoreCreato);
    }

    public Autore findById(Long id) {
        return autoreRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Autore non trovato"));
    }



}
