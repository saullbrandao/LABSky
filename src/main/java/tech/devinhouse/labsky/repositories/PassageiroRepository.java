package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.Passageiro;

import java.util.Optional;

public interface PassageiroRepository extends JpaRepository<Passageiro, String> {
    Optional<Passageiro> findByCpf(String cpf);
}
