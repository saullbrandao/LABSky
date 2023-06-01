package tech.devinhouse.labsky.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.devinhouse.labsky.utils.PassengerClassification;

import java.time.LocalDate;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    @Id
    private String cpf;

    private String nome;

    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private PassengerClassification classificacao;

    private Integer milhas;

    @OneToOne(mappedBy = "passenger", fetch = FetchType.LAZY)
    @JsonManagedReference
    private CheckIn checkIn;
}
