package tech.devinhouse.labsky.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.devinhouse.labsky.models.Seat;
import tech.devinhouse.labsky.services.SeatService;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SeatController.class)
class SeatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeatService seatService;

    @Nested
    @DisplayName("listAll")
    class ListAll {
        @Test
        @DisplayName("Quando houver assentos cadastrados, deve retornar lista de assentos")
        void withSeats() throws Exception {
            List<Seat> seats = List.of(
                    Seat.builder().nome("1A").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1B").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1C").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1D").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1E").disponivel(true).emergencia(false).build(),
                    Seat.builder().nome("1F").disponivel(true).emergencia(false).build()
            );


            Mockito.when(seatService.listAll()).thenReturn(seats);
            mockMvc.perform(get("/assentos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(6)));
        }

        @Test
        @DisplayName("Quando não houver assentos cadastrados, deve retornar lista vazia")
        void withoutSeats() throws Exception {
            mockMvc.perform(get("/assentos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }
}


