package tech.devinhouse.labsky.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import tech.devinhouse.labsky.utils.PassengerClassification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PassengerFullResponseDto(
        String cpf,
        String nome,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        PassengerClassification classificacao,
        Integer milhas,
        String eticket,
        String assento,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataHoraConfirmacao) {
}
