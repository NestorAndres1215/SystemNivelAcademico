package com.example.service;

import java.util.List;

import com.example.constants.Mensaje;
import com.example.exception.ValidacionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.model.Estudiante;
import com.example.model.Nota;
import com.example.repository.NotaRepository;

@Service
@RequiredArgsConstructor
public class NotaService {


    private final NotaRepository notaRepository;

    // ---------------------------
    // LISTAR NOTAS
    // ---------------------------
    public List<Nota> listarNotas() {
        return notaRepository.findAll();
    }

    public List<Nota> obtenerNotasPorEstudiante(Long estudianteId) {
        if (estudianteId == null || estudianteId <= 0) {
            throw new ValidacionException(Mensaje.ESTUDIANTE_ID_INVALIDO);
        }
        return notaRepository.findByEstudianteId(estudianteId);
    }

    public Nota buscarPorEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);
        return notaRepository.findByEstudiante(estudiante)
                .orElseThrow(() -> new ValidacionException(Mensaje.NOTA_ENCONTRADO));
    }

    // ---------------------------
    // GUARDAR NOTA
    // ---------------------------
    public Nota guardar(Nota nota) {
        validarNota(nota);
        nota.setNotaFinal(calcularNotaFinal(nota));
        return notaRepository.save(nota);
    }

    // ---------------------------
    // CALCULAR NOTA FINAL
    // ---------------------------
    public Double calcularNotaFinal(Nota nota) {
        validarNota(nota);
        double promedio = (nota.getNota1() + nota.getNota2() + nota.getNota3()) / 3.0;
        return Math.round(promedio * 100.0) / 100.0; // 2 decimales
    }

    // ---------------------------
    // VALIDACIÓN PRIVADA
    // ---------------------------
    private void validarNota(Nota nota) {
        if (nota == null) {
            throw new ValidacionException(Mensaje.NOTA_NULL);
        }

        if (!esNotaValida(nota.getNota1()) ||
                !esNotaValida(nota.getNota2()) ||
                !esNotaValida(nota.getNota3())) {

            throw new ValidacionException(Mensaje.NOTA_FUERA_DE_RANGO);
        }

        validarEstudiante(nota.getEstudiante());
    }


    private boolean esNotaValida(Double valor) {
        return valor != null && valor >= 0 && valor <= 20;
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getId() == null) {
            throw new ValidacionException(Mensaje.ESTUDIANTE_INVALIDO);
        }
    }
}