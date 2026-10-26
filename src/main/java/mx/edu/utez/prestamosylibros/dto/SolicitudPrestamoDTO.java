package mx.edu.utez.prestamosylibros.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudPrestamoDTO {

    @NotNull(message = "El id del lector es obligatorio")
    private Long lectorId;

    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;

}
