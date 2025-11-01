package com.example.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.constants.Mensaje;

import com.example.exception.ValidacionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.model.Estudiante;
import com.example.model.NivelAcademico;
import com.example.repository.NivelAcademicoRepository;

@Service
@RequiredArgsConstructor
public class NivelAcademicoService {

    private final NivelAcademicoRepository nivelAcademicoRepository;


    public void calcularNivelAcademico(Estudiante estudiante, double promedio) {
        validarEstudiante(estudiante);
        validarPromedio(promedio);

        NivelAcademico nivel = nivelAcademicoRepository.findByEstudiante(estudiante)
                .orElseGet(() -> NivelAcademico.builder()
                        .estudiante(estudiante)
                        .build()
                );


        nivel.setPromedio(promedio);
        nivel.setEstado(promedio >= 13 ? "Aprobado" : "Desaprobado");

        nivelAcademicoRepository.save(nivel);
    }


    public List<NivelAcademico> listar() {
        return nivelAcademicoRepository.findAll();
    }

    public NivelAcademico obtenerPorEstudiante(Long estudianteId) {
        if (estudianteId == null || estudianteId <= 0) {
            throw new ValidacionException(Mensaje.ID_ESTUDIANTE_INVALIDO);
        }

        return nivelAcademicoRepository.findByEstudianteId(estudianteId)
                .orElseThrow(() -> new ValidacionException(Mensaje.NIVEL_NO_ENCONTRADO + estudianteId));
    }

    public List<NivelAcademico> buscarPorEstado(String estado) {
        if (!StringUtils.hasText(estado)) {
            throw new ValidacionException(Mensaje.ESTADO_VACIO);
        }
        return nivelAcademicoRepository.findByEstadoIgnoreCase(estado);
    }

    public long contarNotasRegistradas() {
        return nivelAcademicoRepository.count();
    }

    public Double obtenerPromedioGeneral() {
        Double promedio = nivelAcademicoRepository.obtenerPromedioGeneral();
        return promedio != null ? promedio : 0.0;
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() == null) {
            throw new ValidacionException(Mensaje.ESTUDIANTE_INVALIDO);
        }
    }

    private void validarPromedio(double promedio) {
        if (promedio < 0 || promedio > 20) {
            throw new ValidacionException(Mensaje.PROMEDIO_RANGO);
        }
    }

    public List<NivelAcademico> obtenerTop10Promedios() {
        return nivelAcademicoRepository.obtenerTop10Promedios();
    }

    public List<NivelAcademico> obtenerTop10Aprobados() {
        return nivelAcademicoRepository.obtenerTop10Aprobados();
    }

    public List<NivelAcademico> obtenerTop10Reprobados() {
        return nivelAcademicoRepository.obtenerTop10Reprobados();
    }
    public Map<String, Object> obtenerEstadisticas() {

        Double promedioAprobados = nivelAcademicoRepository.obtenerPromedioAprobados();
        Double promedioReprobados = nivelAcademicoRepository.obtenerPromedioReprobados();

        List<Object[]> conteos = nivelAcademicoRepository.obtenerConteoPorEstado();

        long total = 0;
        long aprobados = 0;
        long reprobados = 0;

        for (Object[] fila : conteos) {
            String estado = (String) fila[0];
            Long cantidad = (Long) fila[1];
            total += cantidad;

            if (estado.equals("APROBADO")) aprobados = cantidad;
            if (estado.equals("REPROBADO")) reprobados = cantidad;
        }

        double porcentajeAprobados = total > 0 ? (aprobados * 100.0) / total : 0.0;
        double porcentajeReprobados = total > 0 ? (reprobados * 100.0) / total : 0.0;

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("promedioAprobados", promedioAprobados != null ? promedioAprobados : 0.0);
        resultado.put("promedioReprobados", promedioReprobados != null ? promedioReprobados : 0.0);
        resultado.put("porcentajeAprobados", porcentajeAprobados);
        resultado.put("porcentajeReprobados", porcentajeReprobados);

        return resultado;
    }

}
