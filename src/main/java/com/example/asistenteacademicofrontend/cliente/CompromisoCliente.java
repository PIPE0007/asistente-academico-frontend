package com.example.asistenteacademicofrontend.cliente;

import com.example.asistenteacademicofrontend.dto.CompromisoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//CLASE: CompromisoCliente
@Service
public class CompromisoCliente {

    private final RestTemplate restTemplate;
    private final String url;

    public CompromisoCliente(RestTemplate restTemplate, @Value("${backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.url = backendUrl + "/api/compromisos";
    }

    public List<CompromisoDTO> listar() {
        CompromisoDTO[] respuesta = restTemplate.getForObject(url, CompromisoDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public List<CompromisoDTO> porEstado(String estado) {
        CompromisoDTO[] respuesta = restTemplate.getForObject(url + "/estado/" + estado, CompromisoDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public CompromisoDTO crear(CompromisoDTO compromiso) {
        return restTemplate.postForObject(url, compromiso, CompromisoDTO.class);
    }

    public void actualizar(Integer id, CompromisoDTO compromiso) {
        restTemplate.put(url + "/" + id, compromiso);
    }

    public void completar(Integer id) {
        restTemplate.put(url + "/" + id + "/completar", null);
    }

    public void eliminar(Integer id) {
        restTemplate.delete(url + "/" + id);
    }
}