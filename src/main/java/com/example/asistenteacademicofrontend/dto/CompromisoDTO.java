package com.example.asistenteacademicofrontend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

//CLASE: CompromisoDTO
@Data
public class CompromisoDTO {
    private Integer idCompromiso;
    private String titulo;
    private String descripcion;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaLimite;
    private String prioridad;
    private String estado;
    private EstudianteDTO estudiante;
    private MateriaDTO materia;
}