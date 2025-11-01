package com.example.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.model.Estudiante;
import com.example.service.EstudianteService;

@Controller
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;


    // Lista de estudiantes
    @GetMapping
    public String listarEstudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
        return "estudiante/lista";
    }

    // Formulario de registro
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        return formularioConModelo(model, new Estudiante(), null);
    }

    // Guardar estudiante
    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante, Model model) {
        try {
            estudianteService.guardar(estudiante);
            return "redirect:/estudiantes";
        } catch (IllegalArgumentException e) {
            return formularioConModelo(model, estudiante, e.getMessage());
        }
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    public String buscarPorNombre(@RequestParam("nombre") String nombre, Model model) {
        List<Estudiante> estudiantes = estudianteService.buscarPorNombre(nombre);
        model.addAttribute("estudiantes", estudiantes);
        return "estudiante/lista";
    }

    // Formulario de edición
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteService.buscarPorId(id);
        return formularioConModelo(model, estudiante, null);
    }

    // Actualizar estudiante
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Estudiante estudiante, Model model) {
        try {
            estudianteService.actualizarEstudiante(id, estudiante);
            return "redirect:/estudiantes";
        } catch (IllegalArgumentException e) {
            return formularioConModelo(model, estudiante, e.getMessage());
        }
    }

    // -----------------------
    // MÉTODO PRIVADO PARA FORMULARIOS
    // -----------------------
    private String formularioConModelo(Model model, Estudiante estudiante, String error) {
        model.addAttribute("estudiante", estudiante);
        if (error != null) model.addAttribute("error", error);
        return "estudiante/formulario";
    }
}
