package mx.edu.utez.prestamosylibros.controller;

import mx.edu.utez.prestamosylibros.model.Lector;
import mx.edu.utez.prestamosylibros.service.LectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lectores")
public class LectorController {

    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    // rehabilitar lector
    @PatchMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        Lector lector = lectorService.rehabilitarLector(id);
        return ResponseEntity.ok(lector);
    }
}