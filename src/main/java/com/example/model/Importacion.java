package com.example.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "importacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Importacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", nullable = false)
    private Long id;

    @NotBlank(message = "El tipo de archivo es obligatorio")
    @Pattern(regexp = "EXCEL|XML", message = "El tipo debe ser EXCEL o XML")
    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El nombre del archivo no puede estar vacío")
    @Column(name = "archivo_nombre", nullable = false, length = 255)
    private String archivoNombre;


}