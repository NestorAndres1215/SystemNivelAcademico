package com.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.exception.ValidacionException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.example.model.NivelAcademico;
import com.example.service.NivelAcademicoService;


@Controller
@RequestMapping("/nivel-academico")
public class NivelAcademicoController {

    private final NivelAcademicoService nivelAcademicoService;

    public NivelAcademicoController(NivelAcademicoService nivelAcademicoService) {
        this.nivelAcademicoService = nivelAcademicoService;
    }

    @GetMapping
    public String listarNiveles(@RequestParam Optional<String> estado, Model model) {
        List<NivelAcademico> niveles;

        try {
            niveles = estado
                    .filter(e -> e != null && !e.isBlank())
                    .map(nivelAcademicoService::buscarPorEstado)
                    .orElseGet(nivelAcademicoService::listar);
        } catch (ValidacionException ex) {
            model.addAttribute("error", ex.getMessage());
            niveles = nivelAcademicoService.listar();
        }

        model.addAttribute("niveles", niveles);
        model.addAttribute("estadoSeleccionado", estado.orElse(""));

        return "nivel-academico/lista";
    }
}
