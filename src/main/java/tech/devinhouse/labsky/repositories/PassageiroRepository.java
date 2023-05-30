package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.Passageiro;

public interface PassageiroRepository extends JpaRepository<Passageiro, String> {
}
