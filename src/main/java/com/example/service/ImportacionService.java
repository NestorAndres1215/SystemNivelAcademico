package com.example.service;

import com.example.exception.ValidacionException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import com.example.model.Importacion;
import com.example.repository.ImportacionRepository;

import java.util.List;

@Service
public class ImportacionService {

    private final ImportacionRepository importacionRepository;

    public ImportacionService(ImportacionRepository importacionRepository) {
        this.importacionRepository = importacionRepository;
    }

    // ---------------------------
    // GUARDAR IMPORTACIÓN
    // ---------------------------
    public Importacion guardar(Importacion importacion) {
        validarImportacion(importacion);
        return importacionRepository.save(importacion);
    }

    // ---------------------------
    // LISTAR TODAS LAS IMPORTACIONES
    // ---------------------------
    public List<Importacion> listar() {
        return importacionRepository.findAll();
    }

    // ---------------------------
    // VALIDACIONES PRIVADAS
    // ---------------------------
    private void validarImportacion(Importacion importacion) {
        if (importacion == null) {
            throw new ValidacionException("La importación no puede ser null");
        }

        if (!StringUtils.hasText(importacion.getArchivoNombre())) {
            throw new ValidacionException("El nombre del archivo es obligatorio");
        }


    }
}
