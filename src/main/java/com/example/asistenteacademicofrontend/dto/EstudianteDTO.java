package com.example.asistenteacademicofrontend.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

//CLASE: EstudianteDTO
@Data
public class EstudianteDTO {
    private Integer idEstudiante;
    private String nombre;
    private String correo;
    private List<MateriaDTO> materias = new ArrayList<>();
}