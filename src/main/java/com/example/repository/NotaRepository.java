package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Estudiante;
import com.example.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByEstudianteId(Long estudianteId);
    Optional<Nota> findByEstudiante(Estudiante estudiante);
}