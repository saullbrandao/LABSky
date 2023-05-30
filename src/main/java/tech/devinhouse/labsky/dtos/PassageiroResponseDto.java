package tech.devinhouse.labsky.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import tech.devinhouse.labsky.utils.ClassificacaoPassageiro;

import java.time.LocalDate;

public record PassageiroResponseDto(
        String cpf,
        String nome,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate dataNascimento,
        ClassificacaoPassageiro classificacao,
        Integer milhas) {
}
