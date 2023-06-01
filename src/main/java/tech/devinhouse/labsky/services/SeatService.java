package tech.devinhouse.labsky.services;

import org.springframework.stereotype.Service;
import tech.devinhouse.labsky.exceptions.seat.SeatNotFoundException;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.repositories.SeatRepository;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> listAll() {
        return seatRepository.findAll();
    }

    public Seat findByNome(String nome) {
        return seatRepository.findByNome(nome).orElseThrow(SeatNotFoundException::new);
    }

    public void updateAvailability(Seat seat) {
        seat.setDisponivel(false);
        seatRepository.save(seat);
    }
}
