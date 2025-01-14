package it.epicode.security.autore;


import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autori")
@RequiredArgsConstructor
public class AutoreController {
    private final AutoreService autoreService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Autore> saveAutore(@RequestBody AutoreInsertDto autore) {
        return new ResponseEntity(autoreService.saveAutore(autore), HttpStatus.CREATED);
    }

    @PostMapping("/libero")
    @PreAuthorize("isAuthenticated()")
    // esempio di come ricare l'utente (di spring security) che è contenuto dentro il token
    // @AuthenticationPrincipal User user questa annotazione restituisce proprio l'utente contenuto nel token
    public ResponseEntity<Autore> saveAutoreLibero(@AuthenticationPrincipal User user,  @RequestBody AutoreInsertDto autore) {
        return new ResponseEntity(autoreService.saveAutore(autore, user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autore> getAutore(@PathVariable Long id) {
        return ResponseEntity.ok(autoreService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<Autore>> getAutori(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(autoreService.findAll(pageable));
    }

}
