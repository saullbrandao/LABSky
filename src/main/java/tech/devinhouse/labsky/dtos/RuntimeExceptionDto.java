package tech.devinhouse.labsky.dtos;

import lombok.Getter;

@Getter
public class RuntimeExceptionDto {
    private final String message;

    public RuntimeExceptionDto(RuntimeException error) {
        this.message = error.getMessage();
    }
}