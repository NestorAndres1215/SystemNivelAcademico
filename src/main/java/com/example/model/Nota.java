package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "nota")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false, updatable = false)
    private Long id;

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(min = 3, max = 100, message = "El curso debe tener entre 3 y 100 caracteres")
    @Column(name = "curso", nullable = false, length = 100)
    private String curso;

    @NotNull(message = "La nota 1 es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20")
    @Column(name = "nota_1", nullable = false)
    private Double nota1;

    @NotNull(message = "La nota 2 es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20")
    @Column(name = "nota_2", nullable = false)
    private Double nota2;

    @NotNull(message = "La nota 3 es obligatoria")
    @DecimalMin(value = "0.0", message = "La nota mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota máxima es 20")
    @Column(name = "nota_3", nullable = false)
    private Double nota3;

    @DecimalMin(value = "0.0", message = "La nota final mínima es 0")
    @DecimalMax(value = "20.0", message = "La nota final máxima es 20")
    @Column(name = "nota_final", nullable = false)
    private Double notaFinal;

    @ManyToOne(optional = false) // No se permite NULOS
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

}