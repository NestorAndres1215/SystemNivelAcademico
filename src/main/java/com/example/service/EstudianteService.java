package com.example.service;

import java.util.List;

import com.example.exception.ValidacionException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.model.Estudiante;
import com.example.repository.EstudianteRepository;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    // ---------------------------
    // LISTAR ESTUDIANTES
    // ---------------------------
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    // ---------------------------
    // GUARDAR ESTUDIANTE
    // ---------------------------
    public Estudiante guardar(Estudiante estudiante) {
        validarEstudiante(estudiante, true);
        return estudianteRepository.save(estudiante);
    }

    // ---------------------------
    // BUSCAR POR ID
    // ---------------------------
    public Estudiante buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException("ID de estudiante inválido");
        }
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new ValidacionException("Estudiante no encontrado con ID: " + id));
    }

    // ---------------------------
    // BUSCAR POR NOMBRE
    // ---------------------------
    public List<Estudiante> buscarPorNombre(String nombre) {
        if (!StringUtils.hasText(nombre)) {
            throw new ValidacionException("El nombre no puede estar vacío");
        }
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // ---------------------------
    // ACTUALIZAR ESTUDIANTE
    // ---------------------------
    public Estudiante actualizarEstudiante(Long id, Estudiante actualizado) {
        Estudiante existente = buscarPorId(id);
        validarEstudiante(actualizado, false);

        existente.setNombre(actualizado.getNombre());
        existente.setApellido(actualizado.getApellido());

        if (actualizado.getDni() != null && !actualizado.getDni().equals(existente.getDni())) {
            validarDni(actualizado.getDni());
            existente.setDni(actualizado.getDni());
        }

        return estudianteRepository.save(existente);
    }

    // ---------------------------
    // CONTAR ESTUDIANTES
    // ---------------------------
    public long contarEstudiantes() {
        return estudianteRepository.count();
    }

    // ---------------------------
    // VALIDACIONES PRIVADAS
    // ---------------------------
    private void validarEstudiante(Estudiante estudiante, boolean esNuevo) {
        if (estudiante == null) {
            throw new ValidacionException("El estudiante no puede ser null");
        }
        if (!StringUtils.hasText(estudiante.getNombre())) {
            throw new ValidacionException("El nombre es obligatorio");
        }
        if (!StringUtils.hasText(estudiante.getApellido())) {
            throw new ValidacionException("El apellido es obligatorio");
        }
        if (esNuevo) {
            validarDni(estudiante.getDni());
        }
    }

    private void validarDni(String dni) {
        if (dni == null || dni.length() != 8) {
            throw new ValidacionException("El DNI debe tener exactamente 8 dígitos");
        }
        if (estudianteRepository.existsByDni(dni)) {
            throw new ValidacionException("El DNI ya está registrado");
        }
    }
}
