package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Movimientos;
import ar.edu.utn.dds.k3003.repositories.HeladeraRepository;
import ar.edu.utn.dds.k3003.repositories.HeladeraMapper;
import ar.edu.utn.dds.k3003.repositories.TemperaturaMapper;
import ar.edu.utn.dds.k3003.repositories.TemperaturaRepository;
import lombok.Getter;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

    private final HeladeraMapper heladeraMapper;
    private final HeladeraRepository heladeraRepository;

    private final TemperaturaMapper temperaturaMapper;
    private final TemperaturaRepository temperaturaRepository;

    private FachadaViandas fachadaViandas;

    public Fachada(){
        this.heladeraRepository = new HeladeraRepository();
        this.heladeraMapper = new HeladeraMapper();
        this.temperaturaMapper = new TemperaturaMapper();
        this.temperaturaRepository = new TemperaturaRepository();
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {

        // Que pasa? Puede venir sin ID, si devuelvo el DTO como viene, puede devolver null
        // pero el repo le asigna un id internamente.

        return heladeraMapper.map( // 3° Mapea la heladera a DTO
                this.heladeraRepository.save(   // 2° Guarda la heladera en el repo
                        heladeraMapper.map(heladeraDTO) // 1° Mapea el DTO a Heladera
                )
        );
    }

    public HeladeraDTO buscarXId(Integer heladeraId) throws NoSuchElementException{
        return this.heladeraMapper.map(
                this.heladeraRepository.findById(heladeraId)
        );
    }

    @Override
    public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {
        Heladera heladera = this.heladeraRepository.findById(heladeraId);
        ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(qrVianda);
        heladera.setCantidadDeViandas(heladera.getCantidadDeViandas()+1);
        heladera.setUltimaApertura(LocalDateTime.now());
        heladera.setUltimoMovimiento(Movimientos.DEPOSITO);
        this.heladeraRepository.modifyHeladera(heladera);
        this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
    }

    @Override
    public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {
        return this.heladeraRepository.findById(heladeraId).getCantidadDeViandas();
    }

    @Override
    public void retirar(RetiroDTO retiro) throws NoSuchElementException {
        Heladera heladera = this.heladeraRepository.findById(retiro.getHeladeraId());
        ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(retiro.getQrVianda());
        heladera.setCantidadDeViandas(heladera.getCantidadDeViandas()-1);
        heladera.setUltimaApertura(LocalDateTime.now());
        heladera.setUltimoMovimiento(Movimientos.RETIRO);
        this.heladeraRepository.modifyHeladera(heladera);
        this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.RETIRADA);
    }

    // desacople la mayor cantidad de logica, pero como tengo los repos separados, necesito validar
    // si la heladera existe o no antes de guardar una temperatura de algo inexistente
    @Override
    public void temperatura(TemperaturaDTO temperatura) {
        if (heladeraRepository.existHeladera(temperatura.getHeladeraId())) {
            this.temperaturaRepository.save(
                    this.temperaturaMapper.map(temperatura)
            );
        }
    }

    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer heladeraId) {
        return this.temperaturaMapper.convertirATemperaturasDTO(
                    this.temperaturaRepository.findByHeladeraId(heladeraId)
        );
    }

    @Override
    public void setViandasProxy(FachadaViandas viandas) {
        this.fachadaViandas = viandas;
    }
}
