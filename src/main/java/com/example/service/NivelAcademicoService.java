package com.example.service;

import java.util.List;

import com.example.exception.ValidacionException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.model.Estudiante;
import com.example.model.NivelAcademico;
import com.example.repository.NivelAcademicoRepository;

@Service
public class NivelAcademicoService {

    private final NivelAcademicoRepository nivelAcademicoRepository;

    public NivelAcademicoService(NivelAcademicoRepository nivelAcademicoRepository) {
        this.nivelAcademicoRepository = nivelAcademicoRepository;
    }

    // ---------------------------
    // CALCULAR O ACTUALIZAR NIVEL ACADÉMICO
    // ---------------------------
    public void calcularNivelAcademico(Estudiante estudiante, double promedio) {
        validarEstudiante(estudiante);
        validarPromedio(promedio);

        NivelAcademico nivel = nivelAcademicoRepository.findByEstudiante(estudiante);
        if (nivel == null) {
            nivel = new NivelAcademico();
            nivel.setEstudiante(estudiante);
        }

        nivel.setPromedio(promedio);
        nivel.setEstado(promedio >= 13 ? "Aprobado" : "Desaprobado");

        nivelAcademicoRepository.save(nivel);
    }

    // ---------------------------
    // LISTAR TODOS LOS NIVELES ACADÉMICOS
    // ---------------------------
    public List<NivelAcademico> listar() {
        return nivelAcademicoRepository.findAll();
    }

    // ---------------------------
    // OBTENER POR ESTUDIANTE
    // ---------------------------
    public NivelAcademico obtenerPorEstudiante(Long estudianteId) {
        if (estudianteId == null || estudianteId <= 0) {
            throw new ValidacionException("ID de estudiante inválido");
        }
        NivelAcademico nivel = nivelAcademicoRepository.findByEstudianteId(estudianteId);
        if (nivel == null) {
            throw new ValidacionException("No se encontró nivel académico para el estudiante con ID: " + estudianteId);
        }
        return nivel;
    }

    // ---------------------------
    // BUSCAR POR ESTADO
    // ---------------------------
    public List<NivelAcademico> buscarPorEstado(String estado) {
        if (!StringUtils.hasText(estado)) {
            throw new ValidacionException("El estado no puede estar vacío");
        }
        return nivelAcademicoRepository.findByEstadoIgnoreCase(estado);
    }

    // ---------------------------
    // CONTAR REGISTROS
    // ---------------------------
    public long contarNotasRegistradas() {
        return nivelAcademicoRepository.count();
    }

    // ---------------------------
    // PROMEDIO GENERAL
    // ---------------------------
    public Double obtenerPromedioGeneral() {
        Double promedio = nivelAcademicoRepository.obtenerPromedioGeneral();
        return promedio != null ? promedio : 0.0;
    }

    // ---------------------------
    // VALIDACIONES PRIVADAS
    // ---------------------------
    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() == null) {
            throw new ValidacionException("Estudiante inválido");
        }
    }

    private void validarPromedio(double promedio) {
        if (promedio < 0 || promedio > 20) {
            throw new ValidacionException("El promedio debe estar entre 0 y 20");
        }
    }
}
