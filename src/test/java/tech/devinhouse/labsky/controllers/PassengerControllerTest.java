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
import tech.devinhouse.labsky.dtos.CheckInRequestDto;
import tech.devinhouse.labsky.dtos.CheckInResponseDto;
import tech.devinhouse.labsky.dtos.PassengerFullResponseDto;
import tech.devinhouse.labsky.dtos.PassengerPartialResponseDto;
import tech.devinhouse.labsky.exceptions.passenger.PassengerDidNotDispatchLuggageException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerIsMinorException;
import tech.devinhouse.labsky.exceptions.passenger.PassengerNotFoundException;
import tech.devinhouse.labsky.exceptions.seat.SeatNotFoundException;
import tech.devinhouse.labsky.exceptions.seat.UnavailableSeatException;
import tech.devinhouse.labsky.mappers.CheckInMapper;
import tech.devinhouse.labsky.mappers.PassengerMapper;
import tech.devinhouse.labsky.models.CheckIn;
import tech.devinhouse.labsky.models.Passenger;
import tech.devinhouse.labsky.services.CheckInService;
import tech.devinhouse.labsky.services.PassengerService;
import tech.devinhouse.labsky.utils.PassengerClassification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
class PassengerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PassengerMapper passengerMapper;
    @MockBean
    private CheckInMapper checkInMapper;
    @MockBean
    private PassengerService passengerService;
    @MockBean
    private CheckInService checkInService;

    @Nested
    @DisplayName("listAll")
    class ListAll {
        @Test
        @DisplayName("Quando houver passageiros cadastrados, deve retornar lista de passageiros")
        void withPassengers() throws Exception {
            List<Passenger> passengers = List.of(
                    Passenger.builder().cpf("111.111.111-11").classificacao(PassengerClassification.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build(),
                    Passenger.builder().cpf("222.222.222-22").classificacao(PassengerClassification.VIP).nome("Jane Doe").milhas(110).dataNascimento(LocalDate.of(1975, 5, 20)).build()
            );
            List<PassengerFullResponseDto> passageirosDTO = List.of(
                    new PassengerFullResponseDto("111.111.111-11", "John Doe", LocalDate.of(1970, 1, 10), PassengerClassification.ASSOCIADO, 100, null, null, null),
                    new PassengerFullResponseDto("222.222.222-22", "Jane Doe", LocalDate.of(1975, 5, 20), PassengerClassification.VIP, 110, null, null, null)
            );


            Mockito.when(passengerService.listAll()).thenReturn(passengers);
            Mockito.when(passengerMapper.map(passengers)).thenReturn(passageirosDTO);
            mockMvc.perform(get("/passageiros")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @Test
        @DisplayName("Quando não houver passageiros cadastrados, deve retornar lista vazia")
        void withoutPassengers() throws Exception {
            mockMvc.perform(get("/passageiros")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        }
    }


    @Nested
    @DisplayName("findByCpf")
    class FindByCpf {
        @Test
        @DisplayName("Quando houver passageiro cadastrado com o cpf informado, deve retornar o passageiro")
        void found() throws Exception {
            Passenger passenger = Passenger.builder().cpf("111.111.111-11").classificacao(PassengerClassification.ASSOCIADO).nome("John Doe").milhas(100).dataNascimento(LocalDate.of(1970, 1, 10)).build();
            PassengerPartialResponseDto passageiroResponse = new PassengerPartialResponseDto("111.111.111-11", "John Doe", LocalDate.of(1970, 1, 10), PassengerClassification.ASSOCIADO, 100);

            Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenReturn(passenger);
            Mockito.when(passengerMapper.passengerToPassengerPartialResponseDto(passenger)).thenReturn(passageiroResponse);

            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.cpf", is(passenger.getCpf())))
                    .andExpect(jsonPath("$.nome", is(passenger.getNome())));
        }

        @Test
        @DisplayName("Quando não houver passageiro cadastrado com o cpf informado, deve retornar erro")
        void notFound() throws Exception {
            Mockito.when(passengerService.findByCpf(Mockito.anyString())).thenThrow(PassengerNotFoundException.class);

            mockMvc.perform(get("/passageiros/{cpf}", "111.111.111-11")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsStringIgnoringCase("Passageiro não encontrado.")));
        }
    }

    @Nested
    @DisplayName("create")
    class Create {


        @Test
        @DisplayName("Quando os dados informados forem válidos, deve retornar sucesso")
        void success() throws Exception {
            CheckInRequestDto confirmacaoRequest = new CheckInRequestDto("111.111.111-11", "1A", true);
            CheckInResponseDto confirmacaoResponse = new CheckInResponseDto(UUID.randomUUID().toString(), LocalDateTime.now());
            String requestJson = objectMapper.writeValueAsString(confirmacaoRequest);

            Mockito.when(checkInService.create(Mockito.any())).thenReturn(new CheckIn());
            Mockito.when(checkInMapper.map(Mockito.any())).thenReturn(confirmacaoResponse);

            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.eticket", containsString(confirmacaoResponse.eticket())));
        }

        @Test
        @DisplayName("Quando não houver passageiro cadastrado com o cpf informado, deve retornar erro")
        void passengerNotFound() throws Exception {
            Mockito.when(checkInService.create(Mockito.any())).thenThrow(PassengerNotFoundException.class);
            CheckInRequestDto checkInRequestDto = new CheckInRequestDto("111.111.111-11", "1A", true);
            String requestJson = objectMapper.writeValueAsString(checkInRequestDto);
            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsStringIgnoringCase("Passageiro não encontrado.")));

        }

        @Test
        @DisplayName("Quando não houver assento cadastrado com o nome informado, deve retornar erro")
        void seatNotFound() throws Exception {
            Mockito.when(checkInService.create(Mockito.any())).thenThrow(SeatNotFoundException.class);
            CheckInRequestDto checkInRequestDto = new CheckInRequestDto("111.111.111-11", "1A", true);
            String requestJson = objectMapper.writeValueAsString(checkInRequestDto);
            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", containsStringIgnoringCase("Assento não encontrado.")));
        }

        @Test
        @DisplayName("Quando o assento informado não estiver disponível, deve retornar erro")
        void seatUnavailable() throws Exception {
            Mockito.when(checkInService.create(Mockito.any())).thenThrow(UnavailableSeatException.class);
            CheckInRequestDto checkInRequestDto = new CheckInRequestDto("111.111.111-11", "1A", true);
            String requestJson = objectMapper.writeValueAsString(checkInRequestDto);
            mockMvc.perform(post("/passageiros/confirmacao")
                            .content(requestJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", containsStringIgnoringCase("O assento informado não está disponível.")));
        }

        @Nested
        @DisplayName("Emergency row")
        class EmergencySeatCase {
            @Test
            @DisplayName("Quando o passageiro informado for menor de idade, deve retornar erro")
            void passengerIsMinor() throws Exception {
                Mockito.when(checkInService.create(Mockito.any())).thenThrow(PassengerIsMinorException.class);
                CheckInRequestDto checkInRequestDto = new CheckInRequestDto("111.111.111-11", "1A", true);
                String requestJson = objectMapper.writeValueAsString(checkInRequestDto);
                mockMvc.perform(post("/passageiros/confirmacao")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", containsStringIgnoringCase("O passageiro informado é menor de idade e portanto não pode escolher assento em fileira de emergência.")));
            }

            @Test
            @DisplayName("Quando o passageiro informado não houver despachado as malas, deve retornar erro")
            void passengerDidNotDispatchLuggage() throws Exception {
                Mockito.when(checkInService.create(Mockito.any())).thenThrow(PassengerDidNotDispatchLuggageException.class);
                CheckInRequestDto checkInRequestDto = new CheckInRequestDto("111.111.111-11", "1A", true);
                String requestJson = objectMapper.writeValueAsString(checkInRequestDto);
                mockMvc.perform(post("/passageiros/confirmacao")
                                .content(requestJson)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message", containsStringIgnoringCase("O passageiro informado não despachou suas malas e portanto não pode escolher assento em fileira de emergência.")));
            }
        }
    }
}