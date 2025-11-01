package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    // Puedes agregar métodos personalizados si los necesitas luego
    boolean existsByDni(String dni);

    List<Estudiante> findByNombreContainingIgnoreCase(String nombre);


    // Buscar estudiantes cuyo apellido contenga el texto (ignora mayúsculas/minúsculas)
    List<Estudiante> findByApellidoContainingIgnoreCase(String apellido);

    // Buscar estudiantes por DNI exacto
    List<Estudiante> findByDni(String dni);
}
