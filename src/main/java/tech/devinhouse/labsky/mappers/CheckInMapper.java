package tech.devinhouse.labsky.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tech.devinhouse.labsky.dtos.CheckInResponseDto;
import tech.devinhouse.labsky.models.CheckIn;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CheckInMapper {
    CheckInResponseDto map(CheckIn checkIn);
}
