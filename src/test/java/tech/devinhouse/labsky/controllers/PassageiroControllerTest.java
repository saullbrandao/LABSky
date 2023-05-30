package tech.devinhouse.labsky.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.devinhouse.labsky.dtos.PassageiroCompletoResponseDto;
import tech.devinhouse.labsky.dtos.PassageiroResponseDto;
import tech.devinhouse.labsky.mappers.PassageiroMapper;
import tech.devinhouse.labsky.models.Passageiro;
import tech.devinhouse.labsky.services.PassageiroService;
import tech.devinhouse.labsky.utils.ClassificacaoPassageiro;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassageiroController.class)
class PassageiroControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassageiroMapper passageiroMapper;
    @MockBean
    private PassageiroService passageiroService;

    @Test
    @DisplayName("Quando houver passageiros cadastrados, deve retornar lista de passageiros")
    void listAll() throws Exception {
        List<Passageiro> passageiros = List.of(
                Passageiro.builder().cpf("111.111.111-11").classificacao(ClassificacaoPassageiro.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build(),
                Passageiro.builder().cpf("222.222.222-22").classificacao(ClassificacaoPassageiro.VIP).nome("Jane Doe").milhas(110).dataNascimento(LocalDate.of(1975, 5, 20)).build()
        );
        List<PassageiroCompletoResponseDto> passageirosDTO = List.of(
                new PassageiroCompletoResponseDto("111.111.111-11", "John Doe", LocalDate.of(1970, 1, 10), ClassificacaoPassageiro.ASSOCIADO, 100, null, null, null),
                new PassageiroCompletoResponseDto("222.222.222-22", "Jane Doe", LocalDate.of(1975, 5, 20), ClassificacaoPassageiro.VIP, 110, null, null, null)
        );


        Mockito.when(passageiroService.listAll()).thenReturn(passageiros);
        Mockito.when(passageiroMapper.map(passageiros)).thenReturn(passageirosDTO);
        mockMvc.perform(get("/passageiros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Quando não houver passageiros cadastrados, deve retornar lista vazia")
    void listAll_empty() throws Exception {
        mockMvc.perform(get("/passageiros")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }


    @Test
    @DisplayName("Quando houver passageiro cadastrado com o cpf informado, deve retornar o passageiro")
    void findByCpf() throws Exception {
        Passageiro passageiro = Passageiro.builder().cpf("111.111.111-11").classificacao(ClassificacaoPassageiro.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build();
        PassageiroResponseDto passageiroResponse = new PassageiroResponseDto("111.111.111-11", "John Doe", LocalDate.of(1970, 1, 10), ClassificacaoPassageiro.ASSOCIADO, 100);

        Mockito.when(passageiroService.findByCpf(Mockito.anyString())).thenReturn(passageiro);
        Mockito.when(passageiroMapper.map(passageiro)).thenReturn(passageiroResponse);

        mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf", is(passageiro.getCpf())));
    }

    @Test
    @DisplayName("Quando não houver passageiro cadastrado com o cpf informado, deve retornar status 404")
    void findByCpf_notFound() throws Exception {
        Mockito.when(passageiroService.findByCpf(Mockito.anyString())).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}