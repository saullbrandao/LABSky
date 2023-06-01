package tech.devinhouse.labsky.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckInRequestDto(
        @NotBlank String cpf,
        @NotBlank String assento,
        @NotNull Boolean malasDespachadas
) {
}
