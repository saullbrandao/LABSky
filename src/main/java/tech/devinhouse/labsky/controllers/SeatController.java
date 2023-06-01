package tech.devinhouse.labsky.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.services.SeatService;

import java.util.List;

@RestController
@RequestMapping(path = "/assentos")
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<Seat> listAll() {
        return seatService.listAll();
    }
}