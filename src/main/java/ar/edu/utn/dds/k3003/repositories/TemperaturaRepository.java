package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Temperatura;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
public class TemperaturaRepository {

    private static AtomicInteger seqId = new AtomicInteger();
    private Collection<Temperatura> temperaturas;

    public TemperaturaRepository() {
        this.temperaturas = new ArrayList<>();
    }

    public Temperatura save(Temperatura temperatura) {
        if (Objects.isNull(temperatura.getId())) {
            temperatura.setId(seqId.getAndIncrement());
            this.temperaturas.add(temperatura);
        }
        return temperatura;
    }

    // a lineas futuras, podría mejorar este metodo para que sea un poco mas declarativo y funcional
    public List<Temperatura> findByHeladeraId(Integer idHeladera) {
        List<Temperatura> temperaturasById = this.temperaturas.stream()
                .filter(x -> x.getHeladeraId().equals(idHeladera))
                .sorted(Comparator.comparingInt(Temperatura::getId).reversed())
                .collect(Collectors.toList());

        if (temperaturasById.isEmpty()) {
            throw new NoSuchElementException(
                    String.format("No hay temperaturas para la heladera de id: %s", idHeladera)
            );
        }

        return temperaturasById;
    }

}
