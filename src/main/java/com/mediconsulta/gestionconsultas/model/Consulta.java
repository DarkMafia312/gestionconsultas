package com.mediconsulta.gestionconsultas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre del paciente debe tener entre 3 y 50 caracteres")
    @Pattern(
            regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$",
            message = "El nombre del paciente no debe contener números ni caracteres especiales"
    )
    private String paciente;

    @NotBlank(message = "El nombre del médico es obligatorio")
    private String medico;

    @NotBlank(message = "Debe especificar una especialidad")
    private String especialidad;

    @NotNull(message = "La fecha y hora de la consulta es obligatoria")
    @Future(message = "No se pueden programar consultas en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String estado = "Pendiente";
}