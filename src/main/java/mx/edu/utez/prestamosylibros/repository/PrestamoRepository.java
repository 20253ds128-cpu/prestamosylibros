package mx.edu.utez.prestamosylibros.repository;

import mx.edu.utez.prestamosylibros.model.EstadoPrestamo;
import mx.edu.utez.prestamosylibros.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    int countByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);

    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);

}
