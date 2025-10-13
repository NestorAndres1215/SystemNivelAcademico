package com.example.controller;

import com.example.exception.ValidacionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.model.Nota;
import com.example.service.EstudianteService;
import com.example.service.NotaService;
import com.example.service.NivelAcademicoService;

@Controller
@RequestMapping("/notas")
public class NotaController {

    private final NotaService notaService;
    private final EstudianteService estudianteService;
    private final NivelAcademicoService nivelAcademicoService;

    public NotaController(NotaService notaService, EstudianteService estudianteService, NivelAcademicoService nivelAcademicoService) {
        this.notaService = notaService;
        this.estudianteService = estudianteService;
        this.nivelAcademicoService = nivelAcademicoService;
    }

    // ---------------------------
    // LISTAR TODAS LAS NOTAS
    // ---------------------------
    @GetMapping
    public String verTodas(Model model) {
        model.addAttribute("notas", notaService.listarNotas());
        return "notas/lista";
    }

    // ---------------------------
    // FORMULARIO NUEVA NOTA
    // ---------------------------
    @GetMapping("/nuevo")
    public String nuevaNota(Model model) {
        model.addAttribute("nota", new Nota());
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
        return "notas/formulario";
    }

    // ---------------------------
    // GUARDAR NOTA
    // ---------------------------
    @PostMapping("/guardar")
    public String guardarNota(@ModelAttribute Nota nota, Model model) {
        try {
            // Validación de la nota


            // Calcular nota final
            Double notaFinal = notaService.calcularNotaFinal(nota);
            nota.setNotaFinal(notaFinal);

            // Guardar nota
            notaService.guardar(nota);

            // Actualizar nivel académico
            nivelAcademicoService.calcularNivelAcademico(nota.getEstudiante(), notaFinal);

            return "redirect:/notas";

        } catch (ValidacionException ex) {
            // Enviar mensaje de error a la vista
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
            model.addAttribute("nota", nota);
            return "notas/formulario";
        }
    }
}
