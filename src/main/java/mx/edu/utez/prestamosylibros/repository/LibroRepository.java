package mx.edu.utez.prestamosylibros.repository;

import mx.edu.utez.prestamosylibros.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}
