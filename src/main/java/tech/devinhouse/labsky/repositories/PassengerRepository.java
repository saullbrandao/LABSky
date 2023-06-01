package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.Passenger;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, String> {
    Optional<Passenger> findByCpf(String cpf);
}
