package com.example.utils;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nota")
public class NotaXML {

    private String nombre;
    private String apellido;
    private String dni;
    private String fechaNacimiento;
    private String curso;
    private double nota1;
    private double nota2;
    private double nota3;

    // ---------------------------
    // GETTERS Y SETTERS
    // ---------------------------

    @XmlElement
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre != null ? nombre.trim() : null; }

    @XmlElement
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido != null ? apellido.trim() : null; }

    @XmlElement
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni != null ? dni.trim() : null; }

    @XmlElement
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento != null ? fechaNacimiento.trim() : null; }

    @XmlElement
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso != null ? curso.trim() : null; }

    @XmlElement
    public double getNota1() { return nota1; }
    public void setNota1(double nota1) { this.nota1 = validarNota(nota1); }

    @XmlElement
    public double getNota2() { return nota2; }
    public void setNota2(double nota2) { this.nota2 = validarNota(nota2); }

    @XmlElement
    public double getNota3() { return nota3; }
    public void setNota3(double nota3) { this.nota3 = validarNota(nota3); }

    // ---------------------------
    // VALIDACIÓN DE NOTAS
    // ---------------------------
    private double validarNota(double valor) {
        if (valor < 0 || valor > 20) {
            throw new IllegalArgumentException("Las notas deben estar entre 0 y 20");
        }
        return valor;
    }
}
