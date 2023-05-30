package tech.devinhouse.labsky.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.devinhouse.labsky.models.Assento;
import tech.devinhouse.labsky.services.AssentoService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssentoController.class)
class AssentoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssentoService assentoService;

    @Test
    @DisplayName("Quando houver assentos cadastrados, deve retornar lista de assentos")
    void listAll() throws Exception {
        List<Assento> assentos = List.of(
                Assento.builder().nome("1A").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1B").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1C").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1D").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1E").disponivel(true).emergencia(false).build(),
                Assento.builder().nome("1F").disponivel(true).emergencia(false).build()
        );


        Mockito.when(assentoService.listAll()).thenReturn(assentos);
        mockMvc.perform(get("/assentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }

    @Test
    @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
    void listAll_empty() throws Exception {
        mockMvc.perform(get("/assentos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

}


