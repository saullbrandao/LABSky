package tech.devinhouse.labsky.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tech.devinhouse.labsky.dtos.PassageiroCompletoResponseDto;
import tech.devinhouse.labsky.dtos.PassageiroResponseDto;
import tech.devinhouse.labsky.models.Passageiro;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassageiroMapper {
    List<PassageiroCompletoResponseDto> map(List<Passageiro> passageiro);

    PassageiroResponseDto map(Passageiro passageiro);
}
