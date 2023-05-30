package tech.devinhouse.labsky.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.devinhouse.labsky.utils.ClassificacaoPassageiro;

import java.time.LocalDate;


@Entity(name = "passageiros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passageiro {
    @Id
    private String cpf;

    private String nome;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private ClassificacaoPassageiro classificacao;

    private Integer milhas;
}
