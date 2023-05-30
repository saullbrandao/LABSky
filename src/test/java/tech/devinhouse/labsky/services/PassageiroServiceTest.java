package tech.devinhouse.labsky.services;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.devinhouse.labsky.models.Passageiro;
import tech.devinhouse.labsky.repositories.PassageiroRepository;
import tech.devinhouse.labsky.utils.ClassificacaoPassageiro;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PassageiroServiceTest {
    @Mock
    private PassageiroRepository passageiroRepository;

    @InjectMocks
    private PassageiroService passageiroService;

    @Test
    @DisplayName("Quando houver passageiros cadastrados, deve retornar lista com passageiros")
    void list() {
        List<Passageiro> passageiros = List.of(
                Passageiro.builder().cpf("111.111.111-11").classificacao(ClassificacaoPassageiro.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build(),
                Passageiro.builder().cpf("222.222.222-22").classificacao(ClassificacaoPassageiro.VIP).nome("Jane Doe").milhas(110).dataNascimento(LocalDate.of(1975, 5, 20)).build()
        );
        Mockito.when(passageiroRepository.findAll()).thenReturn(passageiros);
        List<Passageiro> result = passageiroService.listAll();
        assertFalse(result.isEmpty());
        assertEquals(passageiros.size(), result.size());
    }

    @Test
    @DisplayName("Quando não houver passageiros cadastrados, deve retornar lista vazia")
    void list_empty() {
        List<Passageiro> passageiros = passageiroService.listAll();
        assertTrue(passageiros.isEmpty());
    }

    @Test
    @DisplayName("Quando houver passageiro cadastrado com o cpf informado, deve retornar o passageiro")
    void findByCpf() {
        Mockito.when(passageiroRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.of(new Passageiro()));
        Passageiro passageiro = passageiroService.findByCpf("111.111.111-11");
        assertNotNull(passageiro);
    }

    @Test
    @DisplayName("Quando não houver passageiro cadastrado com o cpf informado, deve lançar exceção")
    void findByCpf_notFound() {
        Mockito.when(passageiroRepository.findByCpf(Mockito.anyString())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> passageiroService.findByCpf("111.111.111-11"));
    }
}