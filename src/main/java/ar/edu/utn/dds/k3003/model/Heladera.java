package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "heladeras")
public class Heladera {

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column
    private String nombre;

    @Min(0)
    @Column
    private Integer cantidadDeViandas;

    @Column(columnDefinition = "DATE")
    private LocalDateTime fechaDeFuncionamiento;

    @Column
    private Boolean estadoOperacional;

    @Column
    private Float ultimaTemperaturaRegistrada;

    @Column(columnDefinition = "DATE")
    private LocalDateTime ultimaApertura;

    @Enumerated(EnumType.STRING)
    private Movimientos ultimoMovimiento;

    @Transient
    private ArrayList<Temperatura> temperaturas;

    public Heladera(){}

    public Heladera(
            Integer id,
            String nombre,
            Integer cantidadDeViandas
    ){
        // Pequeña gran decision de diseño, si no controlo acá que es donde creo la heladera,
        // no se donde delegar la responsabilidad de asignar la cantidad de viandas si no lo trae el DTO
        // la asignacion del id es en el repo porque es autoincremental y viene con el tema de memoria
        // por eso, no lo hago aca
        if(cantidadDeViandas == null){
            cantidadDeViandas = 0;
        }

        this.id = id;
        this.nombre = nombre;
        this.cantidadDeViandas = cantidadDeViandas;
        this.fechaDeFuncionamiento = LocalDateTime.now();
        this.estadoOperacional = true;
        this.ultimaTemperaturaRegistrada = null;
        this.ultimaApertura = null;
        this.ultimoMovimiento = Movimientos.SIN_MOVIMIENTOS;
        this.temperaturas = new ArrayList<>();
        validate();
    }

    public void agregarTemperatura(Temperatura temperatura) {
        this.temperaturas.add(0, temperatura);
    }

    // Otra pequeña gran decision de diseño...
    // Si ponia la notacion de setters, me generaba tambien este setter en especifico;
    // el problema es cuando viene un loco y me quiere sacar una vianda de una heladera con 0 viandas
    // por mas que el qr este bien y toda la bola, es mi responsabilidad decir cuando se puede
    // o no retirar una vianda; entonces si yo tengo una heladera y alguno colo un qr valido,
    // el chiste es no dejar la heladera con -1 viandas....
    public void setCantidadDeViandas(Integer cantidadDeViandas) {
        if (cantidadDeViandas < 0) {
            throw new IllegalArgumentException("La cantidad de viandas no puede ser negativa.");
        }
        if (this.cantidadDeViandas == 0 && cantidadDeViandas < this.cantidadDeViandas) {
            throw new IllegalArgumentException("No se puede decrementar la cantidad de viandas cuando ya es 0.");
        }
        this.cantidadDeViandas = cantidadDeViandas;
    }

    // Muy seguramente cuando tenga que modelar el tema de los modelos de heladera
    // Esto vuele, por el momento, me sirve para rebotar ids, nombres y viandas erroneas...
    private void validate() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<Heladera>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (ConstraintViolation<Heladera> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(" : ")
                        .append(violation.getMessage());
                count++;
                if (count < violations.size()) {
                    sb.append(" & ");
                }
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }

}
