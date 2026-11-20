package com.example.asistenteacademicofrontend.cliente;

import com.example.asistenteacademicofrontend.dto.RecordatorioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

//CLASE: RecordatorioCliente
@Service
public class RecordatorioCliente {

    private final RestTemplate restTemplate;
    private final String url;

    public RecordatorioCliente(RestTemplate restTemplate, @Value("${backend.url}") String backendUrl) {
        this.restTemplate = restTemplate;
        this.url = backendUrl + "/api/recordatorios";
    }

    public List<RecordatorioDTO> listar() {
        RecordatorioDTO[] respuesta = restTemplate.getForObject(url, RecordatorioDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public List<RecordatorioDTO> porCompromiso(Integer idCompromiso) {
        RecordatorioDTO[] respuesta = restTemplate.getForObject(url + "/compromiso/" + idCompromiso, RecordatorioDTO[].class);
        return respuesta == null ? List.of() : Arrays.asList(respuesta);
    }

    public RecordatorioDTO crear(RecordatorioDTO recordatorio) {
        return restTemplate.postForObject(url, recordatorio, RecordatorioDTO.class);
    }

    public void actualizar(Integer id, RecordatorioDTO recordatorio) {
        restTemplate.put(url + "/" + id, recordatorio);
    }

    public void marcarEnviado(Integer id) {
        restTemplate.put(url + "/" + id + "/enviado", null);
    }

    public void eliminar(Integer id) {
        restTemplate.delete(url + "/" + id);
    }
}