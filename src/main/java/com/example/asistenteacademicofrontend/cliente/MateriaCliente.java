package com.example.asistenteacademicofrontend.cliente;

import com.example.asistenteacademicofrontend.dto.CompromisoDTO;
import com.example.asistenteacademicofrontend.dto.MateriaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//CLASE: MateriaCliente
@Service
public class MateriaCliente {

    private final RestTemplate restTemplate;
    private final String url;

    public MateriaCliente(RestTemplate restTemplate, @Value("${backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.url = backendUrl + "/api/materias";
    }

    public List<MateriaDTO> listar() {
        MateriaDTO[] respuesta = restTemplate.getForObject(url, MateriaDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public MateriaDTO crear(MateriaDTO materia) {
        return restTemplate.postForObject(url, materia, MateriaDTO.class);
    }

    public void actualizar(Integer id, MateriaDTO materia) {
        restTemplate.put(url + "/" + id, materia);
    }

    public void eliminar(Integer id) {
        restTemplate.delete(url + "/" + id);
    }

    public List<CompromisoDTO> compromisos(Integer id) {
        CompromisoDTO[] respuesta = restTemplate.getForObject(url + "/" + id + "/compromisos", CompromisoDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }
}