package tech.devinhouse.labsky.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tech.devinhouse.labsky.dtos.CheckInRequestDto;
import tech.devinhouse.labsky.dtos.CheckInResponseDto;
import tech.devinhouse.labsky.dtos.PassengerFullResponseDto;
import tech.devinhouse.labsky.dtos.PassengerPartialResponseDto;
import tech.devinhouse.labsky.mappers.CheckInMapper;
import tech.devinhouse.labsky.mappers.PassengerMapper;
import tech.devinhouse.labsky.models.CheckIn;
import tech.devinhouse.labsky.services.CheckInService;
import tech.devinhouse.labsky.services.PassengerService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/passageiros")
public class PassengerController {
    private final PassengerService passengerService;
    private final CheckInService checkInService;
    private final PassengerMapper passengerMapper;
    private final CheckInMapper checkInMapper;


    public PassengerController(PassengerService passengerService, CheckInService checkInService, PassengerMapper passengerMapper, CheckInMapper checkInMapper) {
        this.passengerService = passengerService;
        this.checkInService = checkInService;
        this.passengerMapper = passengerMapper;
        this.checkInMapper = checkInMapper;
    }

    @GetMapping
    public List<PassengerFullResponseDto> listAll() {
        return passengerMapper.map(passengerService.listAll());
    }

    @GetMapping(path = "/{cpf}")
    public PassengerPartialResponseDto findByCpf(@PathVariable String cpf) {
        return passengerMapper.passengerToPassengerPartialResponseDto(passengerService.findByCpf(cpf));
    }


    @PostMapping(path = "/confirmacao")
    public ResponseEntity<CheckInResponseDto> create(@RequestBody @Valid CheckInRequestDto checkInRequestDto, UriComponentsBuilder uriComponentsBuilder) {
        CheckIn checkIn = checkInService.create(checkInRequestDto);
        URI uri = uriComponentsBuilder.path("/confirmacao/{eticket}").buildAndExpand(checkIn.getEticket()).toUri();
        return ResponseEntity.created(uri).body(checkInMapper.map(checkIn));
    }
}
