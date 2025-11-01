package com.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nivel_academico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NivelAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false, unique = true)
    private Estudiante estudiante;

    @NotNull(message = "El promedio es obligatorio")
    @DecimalMin(value = "0.00", message = "El promedio mínimo es 0")
    @DecimalMax(value = "20.00", message = "El promedio máximo es 20")
    @Column(name = "promedio", nullable = false)
    private Double promedio;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "APROBADO|REPROBADO|EN_CURSO",
            message = "El estado debe ser APROBADO, REPROBADO o EN_CURSO")
    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;


}