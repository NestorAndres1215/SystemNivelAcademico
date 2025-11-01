package com.example.service;

import com.example.constants.Mensaje;
import com.example.exception.ValidacionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import com.example.model.Importacion;
import com.example.repository.ImportacionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportacionService {

    private final ImportacionRepository importacionRepository;

    // GUARDAR IMPORTACIÓN
    public Importacion guardar(Importacion importacion) {
        validarImportacion(importacion);
        return importacionRepository.save(importacion);
    }

    // LISTAR IMPORTACIONES
    public List<Importacion> listar() {
        return importacionRepository.findAll();
    }

    // VALIDACIÓN
    private void validarImportacion(Importacion importacion) {
        if (importacion == null) {
            throw new ValidacionException(Mensaje.ERR_IMPORTACION_NULL);
        }

        if (!StringUtils.hasText(importacion.getTipo())) {
            throw new ValidacionException(Mensaje.ERR_TIPO_OBLIGATORIO);
        }

        String tipoUpper = importacion.getTipo().trim().toUpperCase();
        if (!tipoUpper.equals("EXCEL") && !tipoUpper.equals("XML")) {
            throw new ValidacionException(Mensaje.ERR_TIPO_INVALIDO);
        }
        importacion.setTipo(tipoUpper); // Normalizamos el valor

        if (!StringUtils.hasText(importacion.getArchivoNombre())) {
            throw new ValidacionException(Mensaje.ERR_ARCHIVO_NOMBRE_VACIO);
        }
    }
}