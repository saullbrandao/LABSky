package tech.devinhouse.labsky.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import tech.devinhouse.labsky.utils.PassengerClassification;

import java.time.LocalDate;

public record PassengerPartialResponseDto(
        String cpf,
        String nome,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        PassengerClassification classificacao,
        Integer milhas) {
}
