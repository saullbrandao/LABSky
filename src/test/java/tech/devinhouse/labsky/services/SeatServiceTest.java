package tech.devinhouse.labsky.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.labsky.exceptions.seat.SeatNotFoundException;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.repositories.SeatRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceTest {
    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Nested
    @DisplayName("listAll")
    class ListAll {
        @Test
        @DisplayName("Quando houver assentos cadastrados, deve retornar lista com assentos")
        void withSeats() {
            List<Seat> seats = List.of(
                    Seat.builder().nome("1A").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1B").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1C").disponivel(true).emergencia(false).build()
            );
            Mockito.when(seatRepository.findAll()).thenReturn(seats);
            List<Seat> result = seatService.listAll();
            assertFalse(result.isEmpty());
            assertEquals(seats.size(), result.size());
        }

        @Test
        @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
        void withoutSeats() {
            List<Seat> seats = seatService.listAll();
            assertTrue(seats.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByNome")
    class FindByNome {
        @Test
        @DisplayName("Quando houver assento com o nome informado, deve retornar o assento")
        void found() {
            Mockito.when(seatRepository.findByNome(Mockito.anyString())).thenReturn(Optional.of(new Seat()));
            Seat seat = seatService.findByNome("1A");
            assertNotNull(seat);
        }

        @Test
        @DisplayName("Quando não houver assento com o nome informado, deve lançar exceção")
        void notFound() {
            Mockito.when(seatRepository.findByNome(Mockito.anyString())).thenReturn(Optional.empty());
            assertThrows(SeatNotFoundException.class, () -> seatService.findByNome("1A"));
        }
    }

    @Nested
    @DisplayName("updateAvailability")
    class UpdateAvailability {
        @Test
        @DisplayName("Quando o assento é passado, deve atualizar a disponibilidade do assento")
        void updateAvailability() {
            Seat seat = Seat.builder().disponivel(true).build();
            seatService.updateAvailability(seat);
            assertFalse(seat.isDisponivel());
        }
    }
}
