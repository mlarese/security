package it.epicode.security.autore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoreRepo extends JpaRepository<Autore, Long> {
}
