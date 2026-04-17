package com.mediconsulta.gestionconsultas.repository;
import com.mediconsulta.gestionconsultas.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByFechaHoraAndMedico(LocalDateTime fechaHora, String medico);
}