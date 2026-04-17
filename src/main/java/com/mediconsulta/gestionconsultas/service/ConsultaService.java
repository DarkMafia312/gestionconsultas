package com.mediconsulta.gestionconsultas.service;

import com.mediconsulta.gestionconsultas.model.Consulta;
import com.mediconsulta.gestionconsultas.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository repository;

    public List<Consulta> listarConsultas() {
        return repository.findAll();
    }

    public long total() {
        return repository.count();
    }

    public long hoy() {
        return repository.findAll().stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(LocalDate.now()))
                .count();
    }

    public Consulta registrarConsulta(Consulta consulta) {
        validarFormatoNombre(consulta.getPaciente());

        if (consulta.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se pueden agendar citas en el pasado.");
        }

        if (repository.existsByFechaHoraAndMedico(consulta.getFechaHora(), consulta.getMedico())) {
            throw new RuntimeException("El médico ya tiene una consulta en este horario.");
        }

        validarHorario(consulta);

        return repository.save(consulta);
    }

    private void validarFormatoNombre(String nombre) {
        if (nombre == null || !nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            throw new RuntimeException("El nombre del paciente no debe contener números ni caracteres especiales");
        }
    }

    private void validarHorario(Consulta consulta) {
        int hora = consulta.getFechaHora().getHour();

        boolean esHorarioNormal = (hora >= 6 && hora < 18);
        boolean esHorarioEmergencia = (hora >= 22 || hora < 7);

        if (!esHorarioNormal) {
            if (esHorarioEmergencia && consulta.getMedico().equalsIgnoreCase("Emergencias")) {
            } else {
                throw new RuntimeException("Horario no disponible. Turno normal: 06:00-18:00. Emergencias (Médico Emergencias): 22:00-07:00.");
            }
        }
    }

    public Consulta actualizarConsulta(Long id, Consulta nuevosDatos) {
        return repository.findById(id).map(consulta -> {
            validarFormatoNombre(nuevosDatos.getPaciente());

            validarHorario(nuevosDatos);

            if (nuevosDatos.getFechaHora().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("No se puede actualizar a una fecha pasada.");
            }

            consulta.setPaciente(nuevosDatos.getPaciente());
            consulta.setMedico(nuevosDatos.getMedico());
            consulta.setEspecialidad(nuevosDatos.getEspecialidad());
            consulta.setFechaHora(nuevosDatos.getFechaHora());

            if (nuevosDatos.getEstado() != null) {
                consulta.setEstado(nuevosDatos.getEstado());
            }

            return repository.save(consulta);
        }).orElseThrow(() -> new RuntimeException("No se encontró la consulta con ID: " + id));
    }

    public void cambiarEstado(Long id, String nuevoEstado) {
        Consulta consulta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        consulta.setEstado(nuevoEstado);
        repository.save(consulta);
    }

    public void eliminarConsulta(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("No se pudo eliminar: La cita no existe.");
        }
        repository.deleteById(id);
    }
}