package tech.devinhouse.labsky.services;

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

}
