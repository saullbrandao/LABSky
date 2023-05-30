package tech.devinhouse.labsky.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devinhouse.labsky.dtos.PassageiroResponseDTO;
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
    public List<PassageiroResponseDTO> listAll() {
        return passageiroMapper.map(passageiroService.listAll());
    }
}
