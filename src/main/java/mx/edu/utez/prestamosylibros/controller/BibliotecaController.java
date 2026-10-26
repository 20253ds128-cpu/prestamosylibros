package mx.edu.utez.prestamosylibros.controller;

import jakarta.validation.Valid;
import mx.edu.utez.prestamosylibros.dto.SolicitudPrestamoDTO;
import mx.edu.utez.prestamosylibros.model.Prestamo;
import mx.edu.utez.prestamosylibros.service.BibliotecaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    // solicitar prestamo
    @PostMapping
    public ResponseEntity<Prestamo> solicitarPrestamo(@Valid @RequestBody SolicitudPrestamoDTO dto) {
        Prestamo prestamo = bibliotecaService.solicitarPrestamo(dto);
        return new ResponseEntity<>(prestamo, HttpStatus.CREATED);
    }

    // devolver Libro
    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        Prestamo prestamo = bibliotecaService.devolverLibro(id);
        return ResponseEntity.ok(prestamo);
    }

    // renovar prestamo
    @PatchMapping("/{id}/renovar")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        Prestamo prestamo = bibliotecaService.renovarPrestamo(id);
        return ResponseEntity.ok(prestamo);
    }

    // listar prestamos activos por lector
    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarActivos(@PathVariable Long lectorId) {
        List<Prestamo> prestamos = bibliotecaService.listarPrestamosActivosLector(lectorId);
        return ResponseEntity.ok(prestamos);
    }
}