package tech.devinhouse.labsky.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String eticket;

    @OneToOne
    private Seat seat;

    @OneToOne
    @JsonBackReference
    private Passenger passenger;

    private boolean malasDespachadas;

    @CreationTimestamp
    private LocalDateTime dataHoraConfirmacao;
}
