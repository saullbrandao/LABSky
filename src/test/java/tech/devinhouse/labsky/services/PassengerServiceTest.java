package tech.devinhouse.labsky.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.labsky.exceptions.passenger.PassengerNotFoundException;
import tech.devinhouse.labsky.models.Passenger;
import tech.devinhouse.labsky.repositories.CheckInRepository;
import tech.devinhouse.labsky.repositories.PassengerRepository;
import tech.devinhouse.labsky.utils.PassengerClassification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {
    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    CheckInRepository checkInRepository;

    @Mock
    SeatService seatService;

    @InjectMocks
    private PassengerService passengerService;

    @Nested
    @DisplayName("listAll")
    class ListAll {
        @Test
        @DisplayName("Quando houver passageiros cadastrados, deve retornar lista com passageiros")
        void withPassengers() {
            List<Passenger> passengers = List.of(
                    Passenger.builder().cpf("111.111.111-11").classificacao(PassengerClassification.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build(),
                    Passenger.builder().cpf("222.222.222-22").classificacao(PassengerClassification.VIP).nome("Jane Doe").milhas(110).dataNascimento(LocalDate.of(1975, 5, 20)).build()
            );
            Mockito.when(passengerRepository.findAll()).thenReturn(passengers);
            List<Passenger> result = passengerService.listAll();
            assertFalse(result.isEmpty());
            assertEquals(passengers.size(), result.size());
        }

        @Test
        @DisplayName("Quando não houver passageiros cadastrados, deve retornar lista vazia")
        void withoutPassengers() {
            List<Passenger> passengers = passengerService.listAll();
            assertTrue(passengers.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByCpf")
    class FindByCpf {
        @Test
        @DisplayName("Quando houver passageiro cadastrado com o cpf informado, deve retornar o passageiro")
        void found() {
            Mockito.when(passengerRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.of(new Passenger()));
            Passenger passenger = passengerService.findByCpf("111.111.111-11");
            assertNotNull(passenger);
        }

        @Test
        @DisplayName("Quando não houver passageiro cadastrado com o cpf informado, deve lançar exceção")
        void notFound() {
            Mockito.when(passengerRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.empty());
            assertThrows(PassengerNotFoundException.class, () -> passengerService.findByCpf("111.111.111-11"));
        }
    }

    @Nested
    @DisplayName("updateMilhas")
    class UpdateMilhas {
        @Test
        @DisplayName("Quando um passageiro é passado, deve incrementar as milhas de acordo com a classificação do passageiro")
        void updateMilhas() {
            Passenger passenger = Passenger
                    .builder()
                    .cpf("111.111.111-11")
                    .classificacao(PassengerClassification.ASSOCIADO)
                    .nome("John Doe")
                    .milhas(100)
                    .dataNascimento(LocalDate.of(1970, 1, 10))
                    .build();

            Integer expectedMilhas = passenger.getMilhas() + passenger.getClassificacao().getScore();
            passengerService.updateMilhas(passenger);
            assertEquals(expectedMilhas, passenger.getMilhas());
        }
    }

    @Nested
    @DisplayName("isMinor")
    class IsMinor {
        @Test
        @DisplayName("Quando o passageiro é menor de idade, deve retornar true")
        void isMinor() {
            Passenger passenger = Passenger
                    .builder()
                    .dataNascimento(LocalDate.now().minusYears(17))
                    .build();

            assertTrue(passengerService.isMinor(passenger));
        }

        @Test
        @DisplayName("Quando o passageiro é maior de idade, deve retornar false")
        void isNotMinor() {
            Passenger passenger = Passenger
                    .builder()
                    .dataNascimento(LocalDate.now().minusYears(18))
                    .build();

            assertFalse(passengerService.isMinor(passenger));
        }
    }
}