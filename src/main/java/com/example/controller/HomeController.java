package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.service.EstudianteService;
import com.example.service.NivelAcademicoService;

@Controller
public class HomeController {

    private final EstudianteService estudianteService;
    private final NivelAcademicoService nivelAcademicoService;

    public HomeController(EstudianteService estudianteService, NivelAcademicoService nivelAcademicoService) {
        this.estudianteService = estudianteService;
        this.nivelAcademicoService = nivelAcademicoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalEstudiantes", estudianteService.contarEstudiantes());
        model.addAttribute("totalNotas", nivelAcademicoService.contarNotasRegistradas());
        model.addAttribute("promedioGeneral", nivelAcademicoService.obtenerPromedioGeneral());
        return "index";
    }
}
