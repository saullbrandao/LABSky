package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.Seat;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, String> {
    Optional<Seat> findByNome(String nome);
}
