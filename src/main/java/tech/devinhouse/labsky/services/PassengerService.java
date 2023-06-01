package tech.devinhouse.labsky.services;

import org.springframework.stereotype.Service;
import tech.devinhouse.labsky.exceptions.passenger.PassengerNotFoundException;
import tech.devinhouse.labsky.models.Passenger;
import tech.devinhouse.labsky.repositories.PassengerRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;


    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public List<Passenger> listAll() {
        return passengerRepository.findAll();
    }

    public Passenger findByCpf(String cpf) {
        return passengerRepository.findByCpf(cpf).orElseThrow(PassengerNotFoundException::new);
    }

    public void updateMilhas(Passenger passenger) {
        Integer milhas = passenger.getMilhas();
        Integer score = passenger.getClassificacao().getScore();
        passenger.setMilhas(milhas + score);
        passengerRepository.save(passenger);
    }

    public Boolean isMinor(Passenger passenger) {
        int age = Period.between(passenger.getDataNascimento(), LocalDate.now()).getYears();
        System.out.println(age);
        return age < 18;
    }

}
