package tech.devinhouse.labsky.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "assentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assento {
    @Id
    private String nome;

    private boolean disponivel;

    private boolean emergencia;
}
