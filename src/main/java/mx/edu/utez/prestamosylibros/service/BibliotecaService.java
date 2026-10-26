package mx.edu.utez.prestamosylibros.service;

import mx.edu.utez.prestamosylibros.dto.SolicitudPrestamoDTO;
import mx.edu.utez.prestamosylibros.exception.BadRequestException;
import mx.edu.utez.prestamosylibros.exception.ResourceNotFoundException;
import mx.edu.utez.prestamosylibros.model.EstadoPrestamo;
import mx.edu.utez.prestamosylibros.model.Lector;
import mx.edu.utez.prestamosylibros.model.Libro;
import mx.edu.utez.prestamosylibros.model.Prestamo;
import mx.edu.utez.prestamosylibros.repository.LectorRepository;
import mx.edu.utez.prestamosylibros.repository.LibroRepository;
import mx.edu.utez.prestamosylibros.repository.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BibliotecaService {

    private LibroRepository libroRepository;
    private LectorRepository lectorRepository;
    private PrestamoRepository prestamoRepository;

    public BibliotecaService(LibroRepository libroRepository, LectorRepository lectorRepository, PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
    }

    // solicitar prestamo
    public Prestamo solicitarPrestamo(SolicitudPrestamoDTO dto) {

        // validar que el lector exista y esté activo
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe"));

        if (!lector.isActivo()) {
            throw new BadRequestException("El lector no está activo.");
        }

        // validar que el libro exista y tenga por lo menos 1 ejemplar disponible.
        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("El libro no existe"));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BadRequestException("No hay ejemplares disponibles.");
        }

        // validar que el lector no tenga
        // más de 3 préstamos activos (PRESTADO o VENCIDO)
        // al mismo tiempo.

        // Si es un lector membresiaPremium == true, el límite
        // sube a 5 préstamos.

        List<EstadoPrestamo> estadosActivos = List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        int prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), estadosActivos);

        int limitePermitido = lector.isMembresiaPremium() ? 5 : 3;

        if (prestamosActivos >= limitePermitido) {
            throw new BadRequestException("El lector superó el límite de préstamos activos (" + limitePermitido + ")");
        }

        // restar 1 al libro
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        // crear prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    // devolver libro
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("El libro ya fue devuelto previamente");
        }

        // marcar como devuelto
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        // sumar 1 ejemplar al libro
        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        // regla de negocio: penalización por entrega tardía
        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false); // Desactivar / Bloquear
            lectorRepository.save(lector);
        }

        return prestamoRepository.save(prestamo);
    }

    // renovar préstamo
    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe"));

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("No se puede renovar un préstamo en estado VENCIDO o DEVUELTO");
        }

        // sumar 7 días
        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));

        return prestamoRepository.save(prestamo);
    }

    // listar prestamos activos de un lector
    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException("El lector no existe");
        }

        List<EstadoPrestamo> estadosActivos = List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);
        return prestamoRepository.findByLectorIdAndEstadoIn(lectorId, estadosActivos);
    }
}