package com.example.service;

import java.util.List;

import com.example.constants.Mensaje;
import com.example.exception.ValidacionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.model.Estudiante;
import com.example.repository.EstudianteRepository;

@Service
@RequiredArgsConstructor
public class EstudianteService {
    private final EstudianteRepository estudianteRepository;

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante guardar(Estudiante estudiante) {
        validarEstudiante(estudiante, true);
        return estudianteRepository.save(estudiante);
    }

    public Estudiante buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException(Mensaje.ERR_ID_INVALIDO);
        }
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new ValidacionException(Mensaje.ERR_ESTUDIANTE_NO_ENCONTRADO + id));
    }

    public List<Estudiante> buscarPorNombre(String nombre) {
        if (!StringUtils.hasText(nombre)) {
            throw new ValidacionException(Mensaje.ERR_NOMBRE_VACIO);
        }
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }

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

    public long contarEstudiantes() {
        return estudianteRepository.count();
    }

    private void validarEstudiante(Estudiante estudiante, boolean esNuevo) {
        if (estudiante == null) {
            throw new ValidacionException(Mensaje.ERR_ESTUDIANTE_NULL);
        }
        if (!StringUtils.hasText(estudiante.getNombre())) {
            throw new ValidacionException(Mensaje.ERR_NOMBRE_OBLIGATORIO);
        }
        if (!StringUtils.hasText(estudiante.getApellido())) {
            throw new ValidacionException(Mensaje.ERR_APELLIDO_OBLIGATORIO);
        }
        if (esNuevo) {
            validarDni(estudiante.getDni());
        }
    }

    private void validarDni(String dni) {
        if (dni == null) {
            throw new ValidacionException(Mensaje.ERR_DNI_OBLIGATORIO);
        }
        if (dni.length() != 8) {
            throw new ValidacionException(Mensaje.ERR_DNI_FORMATO);
        }
        if (estudianteRepository.existsByDni(dni)) {
            throw new ValidacionException(Mensaje.ERR_DNI_DUPLICADO);
        }
    }
    public List<Estudiante> listarPorNombre(String nombre) {
        if (!StringUtils.hasText(nombre)) {
            throw new ValidacionException(Mensaje.ERR_NOMBRE_VACIO);
        }
        return estudianteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Estudiante> listarPorApellido(String apellido) {
        if (!StringUtils.hasText(apellido)) {
            throw new ValidacionException(Mensaje.ERR_APELLIDO_VACIO);
        }
        return estudianteRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    public List<Estudiante> listarPorDni(String dni) {
        if (!StringUtils.hasText(dni)) {
            throw new ValidacionException(Mensaje.ERR_DNI_OBLIGATORIO);
        }
        return estudianteRepository.findByDni(dni);
    }
}
