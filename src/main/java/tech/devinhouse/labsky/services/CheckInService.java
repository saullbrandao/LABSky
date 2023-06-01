package tech.devinhouse.labsky.services;

import org.springframework.stereotype.Service;
import tech.devinhouse.labsky.dtos.CheckInRequestDto;
import tech.devinhouse.labsky.exceptions.passenger.PassengerDidNotDispatchLuggageException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerIsMinorException;
import tech.devinhouse.labsky.exceptions.seat.UnavailableSeatException;
import tech.devinhouse.labsky.models.CheckIn;
import tech.devinhouse.labsky.models.Passenger;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.repositories.CheckInRepository;

@Service
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final PassengerService passengerService;
    private final SeatService seatService;

    public CheckInService(CheckInRepository checkInRepository, PassengerService passengerService, SeatService seatService) {
        this.checkInRepository = checkInRepository;
        this.passengerService = passengerService;
        this.seatService = seatService;
    }

    public CheckIn create(CheckInRequestDto confirmacaoRequest) {
        Passenger passenger = passengerService.findByCpf(confirmacaoRequest.cpf());
        Seat seat = seatService.findByNome(confirmacaoRequest.assento());

        validateAssentoAvailability(seat);
        handleEmergenciaCase(seat, passenger, confirmacaoRequest);

        CheckIn checkIn = buildConfirmacao(seat, passenger, confirmacaoRequest);
        CheckIn savedCheckIn = checkInRepository.save(checkIn);
        seatService.updateAvailability(seat);
        passengerService.updateMilhas(passenger);

        logConfirmation(passenger, savedCheckIn);
        return savedCheckIn;
    }

    private void validateAssentoAvailability(Seat seat) {
        if (!seat.isDisponivel()) {
            throw new UnavailableSeatException();
        }
    }

    private void handleEmergenciaCase(Seat seat, Passenger passenger, CheckInRequestDto confirmacaoRequest) {
        if (seat.isEmergencia()) {
            if (passengerService.isMinor(passenger)) {
                throw new PassengerIsMinorException();
            }
            if (!confirmacaoRequest.malasDespachadas()) {
                throw new PassengerDidNotDispatchLuggageException();
            }
        }
    }

    private CheckIn buildConfirmacao(Seat seat, Passenger passenger, CheckInRequestDto confirmacaoRequest) {
        return CheckIn.builder()
                .seat(seat)
                .passenger(passenger)
                .malasDespachadas(confirmacaoRequest.malasDespachadas())
                .build();
    }

    private void logConfirmation(Passenger passenger, CheckIn checkIn) {
        System.out.println("Confirmação feita pelo passageiro de CPF " + passenger.getCpf() + " com e-ticket " + checkIn.getEticket());
    }
}
