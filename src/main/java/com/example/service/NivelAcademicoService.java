package com.example.service;

import java.util.List;

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
}
