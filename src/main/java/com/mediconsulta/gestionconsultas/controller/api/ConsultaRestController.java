package com.mediconsulta.gestionconsultas.controller.api;

import com.mediconsulta.gestionconsultas.model.Consulta;
import com.mediconsulta.gestionconsultas.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsultaRestController {

    private final ConsultaService service;

    @GetMapping
    public List<Consulta> listar() {
        return service.listarConsultas();
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody @Valid Consulta consulta) {
        try {
            Consulta nueva = service.registrarConsulta(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody @Valid Consulta consulta) {
        try {
            Consulta actualizada = service.actualizarConsulta(id, consulta);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        try {
            service.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado a: " + nuevoEstado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminarConsulta(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}