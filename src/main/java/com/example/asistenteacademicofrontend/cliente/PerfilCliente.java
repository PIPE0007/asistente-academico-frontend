package com.example.asistenteacademicofrontend.cliente;

import com.example.asistenteacademicofrontend.dto.PerfilDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//CLASE: PerfilCliente
@Service
public class PerfilCliente {

    private final RestTemplate restTemplate;
    private final String urlBase;

    public PerfilCliente(RestTemplate restTemplate, @Value("${backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.urlBase = backendUrl + "/api/estudiantes";
    }

    private String url(Integer idEstudiante) {
        return urlBase + "/" + idEstudiante + "/perfil";
    }

    public PerfilDTO obtener(Integer idEstudiante) {
        return restTemplate.getForObject(url(idEstudiante), PerfilDTO.class);
    }

    public PerfilDTO crear(Integer idEstudiante, PerfilDTO perfil) {
        return restTemplate.postForObject(url(idEstudiante), perfil, PerfilDTO.class);
    }

    public void actualizar(Integer idEstudiante, PerfilDTO perfil) {
        restTemplate.put(url(idEstudiante), perfil);
    }

    public void eliminar(Integer idEstudiante) {
        restTemplate.delete(url(idEstudiante));
    }
}