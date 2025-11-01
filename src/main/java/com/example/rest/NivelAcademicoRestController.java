package com.example.rest;

import com.example.exception.ValidacionException;
import com.example.model.NivelAcademico;
import com.example.service.NivelAcademicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nivel-academico")
@RequiredArgsConstructor
@Tag(name = "Nivel Academico", description = "Operaciones CRUD para nivel academico")
public class NivelAcademicoRestController {

    private final NivelAcademicoService nivelAcademicoService;

    @Operation(summary = "Listar niveles académicos", description = "Permite filtrar opcionalmente por estado (Aprobado / Desaprobado).")
    @GetMapping("/listar-niveles")
    public ResponseEntity<?> listarNiveles(
            @Parameter(description = "Estado del nivel académico", example = "Aprobado")
            @RequestParam(required = false) String estado
    ) {
        try {
            List<NivelAcademico> niveles;

            if (estado != null && !estado.isBlank()) {
                niveles = nivelAcademicoService.buscarPorEstado(estado);
            } else {
                niveles = nivelAcademicoService.listar();
            }

            return ResponseEntity.ok(niveles);

        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", ex.getMessage())
            );
        }
    }

    @Operation(
            summary = "Obtener nivel académico por ID de estudiante",
            description = "Devuelve el nivel académico correspondiente al estudiante indicado"
    )
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<?> obtenerPorEstudiante(
            @Parameter(description = "ID del estudiante", example = "1")
            @PathVariable Long estudianteId
    ) {
        try {
            NivelAcademico nivel = nivelAcademicoService.obtenerPorEstudiante(estudianteId);
            return ResponseEntity.ok(nivel);

        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", ex.getMessage())
            );
        }
    }


    @Operation(
            summary = "Buscar niveles académicos por estado",
            description = "Devuelve una lista filtrada por estado (Aprobado / Desaprobado)"
    )
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(
            @Parameter(description = "Estado del nivel académico", example = "Aprobado")
            @PathVariable String estado
    ) {
        try {
            List<NivelAcademico> niveles = nivelAcademicoService.buscarPorEstado(estado);
            return ResponseEntity.ok(niveles);

        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", ex.getMessage())
            );
        }
    }

    @Operation(
            summary = "Obtener el promedio general",
            description = "Calcula el promedio global de todos los niveles académicos registrados"
    )
    @GetMapping("/promedio-general")
    public ResponseEntity<?> obtenerPromedioGeneral() {
        try {
            Double promedioGeneral = nivelAcademicoService.obtenerPromedioGeneral();
            return ResponseEntity.ok(Map.of("promedioGeneral", promedioGeneral));

        } catch (ValidacionException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @Operation(
            summary = "Listar todos los niveles académicos",
            description = "Devuelve la lista completa de registros de nivel académico"
    )
    @GetMapping("/listar")
    public ResponseEntity<?> listarNiveles() {
        List<NivelAcademico> niveles = nivelAcademicoService.listar();
        return ResponseEntity.ok(niveles);
    }

    @GetMapping("/top-10")
    @Operation(
            summary = "Obtener el top 10 de promedios",
            description = "Retorna los 10 estudiantes con mayor promedio académico"
    )
    public ResponseEntity<List<NivelAcademico>> obtenerTop10Promedios() {
        return ResponseEntity.ok(nivelAcademicoService.obtenerTop10Promedios());
    }

    @GetMapping("/top-10/aprobados")
    @Operation(
            summary = "Top 10 alumnos aprobados",
            description = "Retorna los 10 estudiantes con mayor promedio cuyo estado es APROBADO"
    )
    public ResponseEntity<List<NivelAcademico>> obtenerTop10Aprobados() {
        return ResponseEntity.ok(nivelAcademicoService.obtenerTop10Aprobados());
    }

    @GetMapping("/top-10/reprobados")
    @Operation(
            summary = "Top 10 alumnos reprobados",
            description = "Retorna los 10 estudiantes con menor promedio cuyo estado es REPROBADO"
    )
    public ResponseEntity<List<NivelAcademico>> obtenerTop10Reprobados() {
        return ResponseEntity.ok(nivelAcademicoService.obtenerTop10Reprobados());
    }

    @GetMapping("/estadisticas")
    @Operation(
            summary = "Estadísticas generales",
            description = "Promedio y porcentaje de alumnos aprobados y reprobados"
    )
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(nivelAcademicoService.obtenerEstadisticas());
    }


}
