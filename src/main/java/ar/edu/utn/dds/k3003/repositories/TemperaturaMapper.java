package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.Temperatura;

import java.util.stream.Collectors;

import java.util.List;

public class TemperaturaMapper {

    public TemperaturaDTO map(Temperatura temperatura){
        return new TemperaturaDTO(
                temperatura.getTemperatura(),
                temperatura.getHeladeraId(),
                temperatura.getFechaMedicion());
    }

    public Temperatura map(TemperaturaDTO temperaturaDTO){
        return new Temperatura(
                temperaturaDTO.getTemperatura(),
                temperaturaDTO.getHeladeraId(),
                temperaturaDTO.getFechaMedicion()
        );
    }

    public List<TemperaturaDTO> convertirATemperaturasDTO(List<Temperatura> temperaturas) {
        return temperaturas.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
