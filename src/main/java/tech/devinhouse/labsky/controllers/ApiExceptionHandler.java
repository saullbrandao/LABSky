package tech.devinhouse.labsky.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.devinhouse.labsky.dtos.ExceptionResponseDto;
import tech.devinhouse.labsky.exceptions.passenger.PassengerDidNotDispatchLuggageException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerIsMinorException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerNotFoundException;
import tech.devinhouse.labsky.exceptions.seat.SeatNotFoundException;
import tech.devinhouse.labsky.exceptions.seat.UnavailableSeatException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handlePassengerNotFoundException() {
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto("Passageiro não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleSeatNotFoundException() {
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto("Assento não encontrado.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(UnavailableSeatException.class)
    public ResponseEntity<ExceptionResponseDto> handleUnavailableSeatException() {
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto("O assento informado não está disponível.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionResponse);
    }

    @ExceptionHandler(PassengerIsMinorException.class)
    public ResponseEntity<ExceptionResponseDto> handlePassengerIsMinorException() {
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto("O passageiro informado é menor de idade e portanto não pode escolher assento em fileira de emergência.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(PassengerDidNotDispatchLuggageException.class)
    public ResponseEntity<ExceptionResponseDto> handlePassengerDidNotDispatchLuggageException() {
        ExceptionResponseDto exceptionResponse = new ExceptionResponseDto("O passageiro informado não despachou suas malas e portanto não pode escolher assento em fileira de emergência.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
