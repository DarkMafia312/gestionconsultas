package com.mediconsulta.gestionconsultas.controller.view;

import com.mediconsulta.gestionconsultas.model.Consulta;
import com.mediconsulta.gestionconsultas.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaViewController {

    private final ConsultaService service;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("consultas", service.listarConsultas());
        model.addAttribute("total", service.total());
        model.addAttribute("hoy", service.hoy());
        return "dashboard"; // Retorna dashboard.html
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("consulta")) {
            model.addAttribute("consulta", new Consulta());
        }
        return "formulario";
    }

    @PostMapping("/registrar")
    public String registrar(@ModelAttribute Consulta consulta, RedirectAttributes redirectAttrs) {
        try {
            service.registrarConsulta(consulta);
            redirectAttrs.addFlashAttribute("success", "¡Consulta agendada con éxito!");
            return "redirect:/consultas"; // Éxito -> Al Dashboard
        } catch (RuntimeException e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
            redirectAttrs.addFlashAttribute("consulta", consulta);
            return "redirect:/consultas/nueva"; // Error -> Al Formulario
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.eliminarConsulta(id);
            ra.addFlashAttribute("success", "La cita ha sido eliminada.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/consultas";
    }

    @GetMapping("/completar/{id}")
    public String completar(@PathVariable Long id, RedirectAttributes ra) {
        service.cambiarEstado(id, "Realizada");
        ra.addFlashAttribute("success", "Cita marcada como completada.");
        return "redirect:/consultas";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        service.cambiarEstado(id, "Cancelada");
        ra.addFlashAttribute("success", "La cita ha sido cancelada.");
        return "redirect:/consultas";
    }
}