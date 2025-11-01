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
    private final NivelAcademicoService nivelAcademicoService;

    // ---------------------------
    // LISTAR NOTAS
    // ---------------------------
    public List<Nota> listarNotas() {
        return notaRepository.findAll();
    }

    public Nota buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidacionException(Mensaje.NOTA_ID_INVALIDO);
        }

        return notaRepository.findById(id)
                .orElseThrow(() -> new ValidacionException(Mensaje.NOTA_NO_ENCONTRADA));
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


    public Nota actualizar(Long id, Nota notaActualizada) {

        Nota notaExistente = notaRepository.findById(id)
                .orElseThrow(() -> new ValidacionException(Mensaje.NOTA_NO_ENCONTRADA));

        // Actualizamos los campos editables
        notaExistente.setCurso(notaActualizada.getCurso());
        notaExistente.setNota1(notaActualizada.getNota1());
        notaExistente.setNota2(notaActualizada.getNota2());
        notaExistente.setNota3(notaActualizada.getNota3());

        // Recalculamos nota final
        Double notaFinal = calcularNotaFinal(notaActualizada);
        notaExistente.setNotaFinal(notaFinal);

        // Actualizamos nivel académico
        nivelAcademicoService.calcularNivelAcademico(notaExistente.getEstudiante(), notaFinal);

        return notaRepository.save(notaExistente);
    }


}