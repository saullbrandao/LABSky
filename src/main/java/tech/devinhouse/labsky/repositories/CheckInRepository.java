package tech.devinhouse.labsky.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.devinhouse.labsky.models.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, String> {
}
