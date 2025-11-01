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

    @Query(
            value = "SELECT * FROM nivel_academico ORDER BY promedio DESC LIMIT 10",
            nativeQuery = true
    )
    List<NivelAcademico> obtenerTop10Promedios();


    @Query(
            value = "SELECT * FROM nivel_academico WHERE estado = 'APROBADO' ORDER BY promedio DESC LIMIT 10",
            nativeQuery = true
    )
    List<NivelAcademico> obtenerTop10Aprobados();

    @Query(
            value = "SELECT * FROM nivel_academico WHERE estado = 'REPROBADO' ORDER BY promedio ASC LIMIT 10",
            nativeQuery = true
    )
    List<NivelAcademico> obtenerTop10Reprobados();
    // Promedio aprobados
    @Query("SELECT AVG(n.promedio) FROM NivelAcademico n WHERE n.estado = 'APROBADO'")
    Double obtenerPromedioAprobados();

    // Promedio reprobados
    @Query("SELECT AVG(n.promedio) FROM NivelAcademico n WHERE n.estado = 'REPROBADO'")
    Double obtenerPromedioReprobados();

    // Conteo agrupado por estado
    @Query("SELECT n.estado, COUNT(n) FROM NivelAcademico n GROUP BY n.estado")
    List<Object[]> obtenerConteoPorEstado();

}