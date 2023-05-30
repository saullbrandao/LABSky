package tech.devinhouse.labsky.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import tech.devinhouse.labsky.models.Passageiro;
import tech.devinhouse.labsky.repositories.PassageiroRepository;

import java.util.List;

@Service
public class PassageiroService {
    private final PassageiroRepository passageiroRepository;

    public PassageiroService(PassageiroRepository passageiroRepository) {
        this.passageiroRepository = passageiroRepository;

    }

    public List<Passageiro> listAll() {
        return passageiroRepository.findAll();
    }

    public Passageiro findByCpf(String cpf) {
        return passageiroRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("Passageiro não encontrado."));
    }

}
