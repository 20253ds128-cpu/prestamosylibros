package mx.edu.utez.prestamosylibros.repository;

import mx.edu.utez.prestamosylibros.model.Lector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectorRepository extends JpaRepository<Lector, Long> {
}
