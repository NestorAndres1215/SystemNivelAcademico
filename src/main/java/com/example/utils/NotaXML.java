package com.example.utils;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "nota")
public class NotaXML {

    @XmlElement
    private String nombre;

    @XmlElement
    private String apellido;

    @XmlElement
    private String dni;

    @XmlElement
    private String fechaNacimiento;

    @XmlElement
    private String curso;

    @XmlElement
    private double nota1;

    @XmlElement
    private double nota2;

    @XmlElement
    private double nota3;

    // ---------------------------
    // VALIDACIÓN POST CONSTRUCCIÓN
    // ---------------------------
    public void normalizarYValidar() {
        this.nombre = limpiar(nombre);
        this.apellido = limpiar(apellido);
        this.dni = limpiar(dni);
        this.fechaNacimiento = limpiar(fechaNacimiento);
        this.curso = limpiar(curso);

        this.nota1 = validar(nota1);
        this.nota2 = validar(nota2);
        this.nota3 = validar(nota3);
    }

    private String limpiar(String valor) {
        return valor != null ? valor.trim() : null;
    }

    private double validar(double valor) {
        if (valor < 0 || valor > 20) {
            throw new IllegalArgumentException("Las notas deben estar entre 0 y 20");
        }
        return valor;
    }
}
