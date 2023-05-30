package tech.devinhouse.labsky.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devinhouse.labsky.dtos.PassageiroCompletoResponseDto;
import tech.devinhouse.labsky.dtos.PassageiroResponseDto;
import tech.devinhouse.labsky.mappers.PassageiroMapper;
import tech.devinhouse.labsky.services.PassageiroService;

import java.util.List;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {
    private final PassageiroService passageiroService;
    private final PassageiroMapper passageiroMapper;

    public PassageiroController(PassageiroService passageiroService, PassageiroMapper passageiroMapper) {
        this.passageiroService = passageiroService;
        this.passageiroMapper = passageiroMapper;
    }

    @GetMapping
    public List<PassageiroCompletoResponseDto> listAll() {
        return passageiroMapper.map(passageiroService.listAll());
    }

    @GetMapping(path = "/{cpf}")
    public PassageiroResponseDto findById(@PathVariable String cpf) {
        return passageiroMapper.map(passageiroService.findByCpf(cpf));
    }
}
