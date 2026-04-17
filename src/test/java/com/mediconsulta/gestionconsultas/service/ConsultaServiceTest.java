package com.mediconsulta.gestionconsultas.service;

import com.mediconsulta.gestionconsultas.model.Consulta;
import com.mediconsulta.gestionconsultas.repository.ConsultaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultaServiceTest {

    @Mock
    private ConsultaRepository repository;

    @InjectMocks
    private ConsultaService service;

    @Test
    @DisplayName("Debe lanzar excepción cuando el nombre del paciente contiene números o caracteres especiales")
    void testNombrePacienteInvalido() {
        LocalDateTime fechaFutura = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        Consulta consultaInvalida = new Consulta(null, "Alvaro 123!", "Dr. Smith", "General", fechaFutura, "Pendiente");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.registrarConsulta(consultaInvalida));

        assertEquals("El nombre del paciente no debe contener números ni caracteres especiales", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el médico ya tiene una cita en ese horario")
    void testEvitarDuplicidad() {
        LocalDateTime fechaFutura = LocalDateTime.now().plusDays(10).withHour(10).withMinute(0);
        Consulta consulta = new Consulta(null, "Juan Perez", "Dra. Garcia", "Pediatría", fechaFutura, "Pendiente");

        when(repository.existsByFechaHoraAndMedico(fechaFutura, "Dra. Garcia")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.registrarConsulta(consulta));

        assertEquals("El médico ya tiene una consulta en este horario.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la fecha es en el pasado")
    void testFechaPasada() {
        LocalDateTime fechaPasada = LocalDateTime.now().minusDays(1);
        Consulta consultaInvalida = new Consulta(null, "Paciente Valido", "Medico", "General", fechaPasada, "Pendiente");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.registrarConsulta(consultaInvalida));

        assertEquals("No se pueden agendar citas en el pasado.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe validar correctamente el horario de Emergencias en la madrugada")
    void testHorarioEmergenciaExitoso() {
        LocalDateTime madrugada = LocalDateTime.now().plusDays(1).withHour(2).withMinute(0);
        Consulta consultaEmergencia = new Consulta(null, "Paciente Critico", "Emergencias", "Urgencias", madrugada, "Pendiente");

        when(repository.existsByFechaHoraAndMedico(madrugada, "Emergencias")).thenReturn(false);
        when(repository.save(any(Consulta.class))).thenReturn(consultaEmergencia);

        Consulta resultado = service.registrarConsulta(consultaEmergencia);

        assertEquals("Emergencias", resultado.getMedico());
        verify(repository).save(any(Consulta.class));
    }

    @Test
    @DisplayName("Debe lanzar error si un médico normal intenta atender de noche")
    void testHorarioInvalidoEspecialista() {
        LocalDateTime noche = LocalDateTime.now().plusDays(1).withHour(23).withMinute(0);
        Consulta consulta = new Consulta(null, "Ana Maria", "Dr. Smith", "Cardiología", noche, "Pendiente");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.registrarConsulta(consulta));

        assertEquals("Horario no disponible. Turno normal: 06:00-18:00. Emergencias (Médico Emergencias): 22:00-07:00.", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar error al intentar cambiar estado de cita inexistente")
    void testCambiarEstadoInexistente() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.cambiarEstado(id, "Realizada"));

        assertEquals("Cita no encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Debe eliminar la consulta si existe")
    void testEliminarExitoso() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.eliminarConsulta(id);

        verify(repository).deleteById(id);
    }
}