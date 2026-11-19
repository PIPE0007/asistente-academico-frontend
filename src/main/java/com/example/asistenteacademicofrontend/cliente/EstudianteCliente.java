package com.example.asistenteacademicofrontend.cliente;

import com.example.asistenteacademicofrontend.dto.EstudianteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//CLASE: EstudianteCliente
@Service
public class EstudianteCliente {

    private final RestTemplate restTemplate;
    private final String url;

    public EstudianteCliente(RestTemplate restTemplate, @Value("${backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.url = backendUrl + "/api/estudiantes";
    }

    public List<EstudianteDTO> listar() {
        EstudianteDTO[] respuesta = restTemplate.getForObject(url, EstudianteDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public EstudianteDTO crear(EstudianteDTO estudiante) {
        return restTemplate.postForObject(url, estudiante, EstudianteDTO.class);
    }

    public void actualizar(Integer id, EstudianteDTO estudiante) {
        restTemplate.put(url + "/" + id, estudiante);
    }

    public void eliminar(Integer id) {
        restTemplate.delete(url + "/" + id);
    }
}