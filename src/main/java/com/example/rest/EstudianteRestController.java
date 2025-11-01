package com.example.rest;

import com.example.exception.ValidacionException;
import com.example.model.Estudiante;
import com.example.service.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiante", description = "Operaciones CRUD para estudiante")
public class EstudianteRestController {

    private final EstudianteService estudianteService;

    @Operation(summary = "Listar todos los estudiantes")
    @GetMapping
    public ResponseEntity<List<Estudiante>> listarEstudiantes() {
        return ResponseEntity.ok(estudianteService.listarEstudiantes());
    }

    @Operation(summary = "Buscar estudiante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(
            @Parameter(description = "ID del estudiante", example = "1")
            @PathVariable Long id
    ) {
        try {
            return ResponseEntity.ok(estudianteService.buscarPorId(id));
        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(summary = "Buscar estudiantes por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(
            @Parameter(description = "Nombre del estudiante", example = "Juan")
            @RequestParam String nombre
    ) {
        try {
            return ResponseEntity.ok(estudianteService.buscarPorNombre(nombre));
        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(summary = "Registrar un estudiante")
    @PostMapping
    public ResponseEntity<?> guardar(
            @Valid @RequestBody Estudiante estudiante
    ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.guardar(estudiante));
        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(summary = "Actualizar un estudiante")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del estudiante", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody Estudiante estudiante
    ) {
        try {
            return ResponseEntity.ok(estudianteService.actualizarEstudiante(id, estudiante));
        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(summary = "Contar cuántos estudiantes existen")
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> contar() {
        return ResponseEntity.ok(Map.of("totalEstudiantes", estudianteService.contarEstudiantes()));
    }
    @Operation(
            summary = "Listar estudiantes por nombre",
            description = "Devuelve una lista de estudiantes cuyo nombre contenga el texto proporcionado"
    )
    @GetMapping("/nombre")
    public ResponseEntity<List<Estudiante>> listarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", example = "Juan")
            @RequestParam String nombre
    ) {
        return ResponseEntity.ok(estudianteService.listarPorNombre(nombre));
    }

    @Operation(
            summary = "Listar estudiantes por apellido",
            description = "Devuelve una lista de estudiantes cuyo apellido contenga el texto proporcionado"
    )
    @GetMapping("/apellido")
    public ResponseEntity<List<Estudiante>> listarPorApellido(
            @Parameter(description = "Texto a buscar en el apellido", example = "Perez")
            @RequestParam String apellido
    ) {
        return ResponseEntity.ok(estudianteService.listarPorApellido(apellido));
    }

    @Operation(
            summary = "Listar estudiantes por DNI",
            description = "Devuelve una lista de estudiantes que tengan el DNI exacto proporcionado"
    )
    @GetMapping("/dni")
    public ResponseEntity<List<Estudiante>> listarPorDni(
            @Parameter(description = "DNI exacto del estudiante", example = "12345678")
            @RequestParam String dni
    ) {
        return ResponseEntity.ok(estudianteService.listarPorDni(dni));
    }
}
