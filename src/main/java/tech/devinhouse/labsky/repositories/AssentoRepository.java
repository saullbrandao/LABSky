package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.Assento;

public interface AssentoRepository extends JpaRepository<Assento, String> {
}
