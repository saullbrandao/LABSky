package tech.devinhouse.labsky.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tech.devinhouse.labsky.dtos.PassengerFullResponseDto;
import tech.devinhouse.labsky.dtos.PassengerPartialResponseDto;
import tech.devinhouse.labsky.models.Passenger;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassengerMapper {
    @Mapping(target = "eticket", source = "checkIn.eticket")
    @Mapping(target = "assento", source = "checkIn.seat.nome")
    @Mapping(target = "dataHoraConfirmacao", source = "checkIn.dataHoraConfirmacao")
    PassengerFullResponseDto map(Passenger passenger);

    List<PassengerFullResponseDto> map(List<Passenger> passengerList);

    PassengerPartialResponseDto passengerToPassengerPartialResponseDto(Passenger passenger);
}
