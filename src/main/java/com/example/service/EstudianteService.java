package com.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.Estudiante;
import com.example.repository.EstudianteRepository;

@Service
public class EstudianteService {

    private final EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {
        // Validar longitud de DNI
        if (estudiante.getDni() == null || estudiante.getDni().length() != 8) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 9 dígitos.");
        }

        // Validar si el DNI ya existe
        if (estudianteRepository.existsByDni(estudiante.getDni())) {
            throw new IllegalArgumentException("El DNI ya está registrado.");
        }

        return estudianteRepository.save(estudiante);
    }

    public Estudiante buscarPorId(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public List<Estudiante> buscarPorNombre(String nombre) {
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Estudiante actualizarEstudiante(Long id, Estudiante actualizado) {
        Estudiante existente = buscarPorId(id);
        if (existente != null) {
            existente.setNombre(actualizado.getNombre());
            existente.setApellido(actualizado.getApellido());

            if (actualizado.getDni() != null && !actualizado.getDni().equals(existente.getDni())) {
                // Validar DNI nuevo
                if (actualizado.getDni().length() != 9) {
                    throw new IllegalArgumentException("El nuevo DNI debe tener 9 dígitos.");
                }
                if (estudianteRepository.existsByDni(actualizado.getDni())) {
                    throw new IllegalArgumentException("El nuevo DNI ya está registrado.");
                }
                existente.setDni(actualizado.getDni());
            }

            return estudianteRepository.save(existente);
        }
        return null;
    }

    public long contarEstudiantes() {
        return estudianteRepository.count();
    }
}