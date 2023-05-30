package tech.devinhouse.labsky.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.labsky.models.Assento;
import tech.devinhouse.labsky.repositories.AssentoRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AssentoServiceTest {
    @Mock
    private AssentoRepository assentoRepository;

    @InjectMocks
    private AssentoService assentoService;

    @Test
    @DisplayName("Quando houver assentos cadastrados, deve retornar lista com assentos")
    void list() {
        List<Assento> assentos = List.of(
                Assento.builder().nome("1A").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1B").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1C").disponivel(true).emergencia(false).build()
        );
        Mockito.when(assentoRepository.findAll()).thenReturn(assentos);
        List<Assento> result = assentoService.listAll();
        assertFalse(result.isEmpty());
        assertEquals(assentos.size(), result.size());
    }

    @Test
    @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
    void list_empty() {
        List<Assento> assentos = assentoService.listAll();
        assertTrue(assentos.isEmpty());
    }
}
