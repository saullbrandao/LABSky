package tech.devinhouse.labsky.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CheckInResponseDto(
        String eticket,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime dataHoraConfirmacao
) {
}
