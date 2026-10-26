package mx.edu.utez.prestamosylibros.service;

import mx.edu.utez.prestamosylibros.exception.ResourceNotFoundException;
import mx.edu.utez.prestamosylibros.model.Lector;
import mx.edu.utez.prestamosylibros.repository.LectorRepository;
import org.springframework.stereotype.Service;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new ResourceNotFoundException("El lector con el id: " + lectorId + " no extiste."));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }
}
