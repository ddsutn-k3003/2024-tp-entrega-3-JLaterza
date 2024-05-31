package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class Temperatura {

    @Min(0)
    private Integer id;

    @NotNull
    private Integer temperatura;

    @NotNull
    @Min(0)
    private Integer heladeraId;

    @NotNull
    private LocalDateTime fechaMedicion;

    /* Murio este constructor
    public Temperatura(Integer temperatura, Integer heladeraId){
        this.id = null;
        this.temperatura = temperatura;
        this.heladeraId = heladeraId;
        this.fechaMedicion = LocalDateTime.now();
    }
     */

    public Temperatura(Integer temperatura, Integer heladeraId, LocalDateTime fechaMedicion){
        this.id = null;
        this.temperatura = temperatura;
        this.heladeraId = heladeraId;
        this.fechaMedicion = fechaMedicion;
        validate();
    }

    // IDEM heladeras...
    private void validate() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<Temperatura>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (ConstraintViolation<Temperatura> violation : violations) {
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
