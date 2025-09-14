package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table (name = "tipo")
public enum TipoObra_domain {
    filme,
    serie
}
