package tech.devinhouse.labsky.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.labsky.dtos.CheckInRequestDto;
import tech.devinhouse.labsky.exceptions.passenger.PassengerDidNotDispatchLuggageException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerIsMinorException;
import tech.devinhouse.labsky.exceptions.seat.UnavailableSeatException;
import tech.devinhouse.labsky.models.CheckIn;
import tech.devinhouse.labsky.models.Passenger;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.repositories.CheckInRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class CheckInServiceTest {
    @Mock
    private CheckInRepository checkInRepository;

    @Mock
    private SeatService seatService;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private CheckInService checkInService;


    @Nested
    @DisplayName("create")
    class Create {
        @Test
        @DisplayName("Quando os dados informados forem válidos, deve retornar a confirmação")
        void success() {
            CheckInRequestDto confirmacaoRequest = new CheckInRequestDto("111.111.111-11", "1A", true);
            Passenger passenger = Passenger.builder().cpf("111.111.111-11").build();
            Seat seat = Seat.builder().nome("1A").disponivel(true).build();
            CheckIn checkIn = CheckIn
                    .builder()
                    .eticket(UUID.randomUUID().toString())
                    .seat(seat)
                    .passenger(passenger)
                    .build();

            Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenReturn(passenger);
            Mockito.when(seatService.findByNome(Mockito.anyString())).thenReturn(seat);
            Mockito.when(checkInRepository.save(Mockito.any())).thenReturn(checkIn);

            CheckIn createdCheckIn = checkInService.create(confirmacaoRequest);

            assertEquals(passenger.getCpf(), createdCheckIn.getPassenger().getCpf());
            assertEquals(seat.getNome(), createdCheckIn.getSeat().getNome());
            assertNotNull(createdCheckIn.getEticket());
        }


        @Test
        @DisplayName("Quando o assento informado não estiver disponível, deve lançar exceção")
        void unavailableSeat() {
            CheckInRequestDto confirmacaoRequest = new CheckInRequestDto("111.111.111-11", "1A", true);
            Seat seat = Seat.builder().nome("1A").disponivel(false).build();

            Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenReturn(new Passenger());
            Mockito.when(seatService.findByNome(Mockito.anyString())).thenReturn(seat);

            assertThrows(UnavailableSeatException.class, () -> checkInService.create(confirmacaoRequest));
        }

        @Nested
        @DisplayName("Quando o assento selecionado está em fileira de emergência")
        class EmergencySeatCase {
            @Test
            @DisplayName("Quando o passageiro informado for menor de idade, deve lançar exceção")
            void passengerIsMinor() {
                CheckInRequestDto confirmacaoRequest = new CheckInRequestDto("111.111.111-11", "1A", true);
                Passenger passenger = Passenger.builder().cpf("111.111.111-11").dataNascimento(LocalDate.now().minusYears(17)).build();
                Seat seat = Seat.builder().nome("5A").disponivel(true).emergencia(true).build();

                Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenReturn(passenger);
                Mockito.when(passengerService.isMinor(Mockito.any())).thenReturn(true);
                Mockito.when(seatService.findByNome(Mockito.anyString())).thenReturn(seat);

                assertThrows(PassengerIsMinorException.class, () -> checkInService.create(confirmacaoRequest));
            }

            @Test
            @DisplayName("Quando o passageiro informado não houver despachado as malas, deve lançar exceção")
            void passengerDidNotDispatchBags() {
                CheckInRequestDto confirmacaoRequest = new CheckInRequestDto("111.111.111-11", "1A", false);
                Passenger passenger = Passenger.builder().cpf("111.111.111-11").dataNascimento(LocalDate.now().minusYears(17)).build();
                Seat seat = Seat.builder().nome("5A").disponivel(true).emergencia(true).build();

                Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenReturn(passenger);
                Mockito.when(passengerService.isMinor(Mockito.any())).thenReturn(false);
                Mockito.when(seatService.findByNome(Mockito.anyString())).thenReturn(seat);

                assertThrows(PassengerDidNotDispatchLuggageException.class, () -> checkInService.create(confirmacaoRequest));
            }
        }
    }
}
