package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.model.Estudiante;
import com.example.model.NivelAcademico;

public interface NivelAcademicoRepository extends JpaRepository<NivelAcademico, Long> {

    Optional<NivelAcademico> findByEstudiante(Estudiante estudiante);
    Optional<NivelAcademico> findByEstudianteId(Long estudianteId);

    List<NivelAcademico> findByEstado(String estado);

    List<NivelAcademico> findByEstadoIgnoreCase(String estado);

    @Query("SELECT AVG(n.promedio) FROM NivelAcademico n")
    Double obtenerPromedioGeneral();
}