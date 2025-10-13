package com.example.service;

import java.util.List;

import com.example.exception.ValidacionException;
import org.springframework.stereotype.Service;

import com.example.model.Estudiante;
import com.example.model.Nota;
import com.example.repository.NotaRepository;

@Service
public class NotaService {


    private final NotaRepository notaRepository;

    public NotaService(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    // ---------------------------
    // LISTAR NOTAS
    // ---------------------------
    public List<Nota> listarNotas() {
        return notaRepository.findAll();
    }

    public List<Nota> obtenerNotasPorEstudiante(Long estudianteId) {
        if (estudianteId == null || estudianteId <= 0) {
            throw new ValidacionException("ID de estudiante inválido");
        }
        return notaRepository.findByEstudianteId(estudianteId);
    }

    public Nota buscarPorEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() == null) {
            throw new ValidacionException("Estudiante inválido");
        }
        return notaRepository.findByEstudiante(estudiante);
    }

    // ---------------------------
    // GUARDAR NOTA
    // ---------------------------
    public Nota guardar(Nota nota) {
        validarNota(nota);
        return notaRepository.save(nota);
    }

    // ---------------------------
    // CALCULAR NOTA FINAL
    // ---------------------------
    public Double calcularNotaFinal(Nota nota) {
        validarNota(nota); // asegurar que las notas sean válidas
        double promedio = (nota.getNota1() + nota.getNota2() + nota.getNota3()) / 3.0;
        return Math.round(promedio * 100.0) / 100.0; // redondeado a 2 decimales
    }

    // ---------------------------
    // VALIDACIÓN PRIVADA
    // ---------------------------
    private void validarNota(Nota nota) {
        if (nota == null) {
            throw new ValidacionException("La nota no puede ser null");
        }

        if (!esNotaValida(nota.getNota1())) {
            throw new ValidacionException("Nota1 inválida: debe estar entre 0 y 20");
        }
        if (!esNotaValida(nota.getNota2())) {
            throw new ValidacionException("Nota2 inválida: debe estar entre 0 y 20");
        }
        if (!esNotaValida(nota.getNota3())) {
            throw new ValidacionException("Nota3 inválida: debe estar entre 0 y 20");
        }
    }

    private boolean esNotaValida(Double valor) {
        return valor != null && valor >= 0 && valor <= 20;
    }
    
}