package com.example.rest;

import com.example.exception.ValidacionException;
import com.example.model.Estudiante;
import com.example.model.Nota;
import com.example.service.EstudianteService;
import com.example.service.NivelAcademicoService;
import com.example.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/notas")
@RequiredArgsConstructor
@Tag(name = "Notas", description = "Operaciones CRUD para archivos")
public class NotaRestController {

    private final NotaService notaService;
    private final EstudianteService estudianteService;
    private final NivelAcademicoService nivelAcademicoService;

    @Operation(summary = "Obtener todos las notas", description = "Devuelve la lista completa de notas")
    @GetMapping
    public List<Nota> listar(Model model) {

        return notaService.listarNotas();
    }

    @Operation(summary = "Registrar una nueva nota", description = "Guarda una nota y actualiza el nivel académico del estudiante")
    @PostMapping("/registrar")
    public ResponseEntity<Nota> guardarNota(@RequestBody Nota nota) {

        Double notaFinal = notaService.calcularNotaFinal(nota);
        nota.setNotaFinal(notaFinal);

        Nota notaGuardada = notaService.guardar(nota);

        nivelAcademicoService.calcularNivelAcademico(nota.getEstudiante(), notaFinal);

        return ResponseEntity.status(HttpStatus.CREATED).body(notaGuardada);
    }

    @Operation(summary = "Buscar nota única por estudiante", description = "Retorna la nota registrada para un estudiante")
    @GetMapping("/buscar")
    public ResponseEntity<Nota> buscarPorEstudiante(@RequestBody Estudiante estudiante) {
        Nota nota = notaService.buscarPorEstudiante(estudiante);
        return ResponseEntity.ok(nota);
    }

    @Operation(summary = "Obtener una nota por ID", description = "Devuelve una nota específica según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Nota> obtenerNotaPorId(@PathVariable Long id) {
        Nota nota = notaService.buscarPorId(id);
        return ResponseEntity.ok(nota);
    }

    @Operation(summary = "Actualizar una nota", description = "Actualiza una nota existente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarNota(
            @PathVariable Long id,
            @Valid @RequestBody Nota nota) {

        try {
            Nota notaActualizada = notaService.actualizar(id, nota);
            return ResponseEntity.ok(notaActualizada);

        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

}
