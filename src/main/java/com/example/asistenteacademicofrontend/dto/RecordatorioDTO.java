package com.example.asistenteacademicofrontend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDate;

//CLASE: RecordatorioDTO
@Data
public class RecordatorioDTO {
    private Integer idRecordatorio;
    private String mensaje;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRecordatorio;
    private boolean enviado;
    private Integer idCompromiso;
}
