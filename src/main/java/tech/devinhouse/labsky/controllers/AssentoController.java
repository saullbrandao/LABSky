package tech.devinhouse.labsky.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.devinhouse.labsky.models.Assento;
import tech.devinhouse.labsky.services.AssentoService;

import java.util.List;

@RestController
@RequestMapping(path = "/assentos")
public class AssentoController {
    private final AssentoService assentoService;

    public AssentoController(AssentoService assentoService) {
        this.assentoService = assentoService;
    }

    @GetMapping
    public List<Assento> listAll() {
        return assentoService.listAll();
    }
}