package tech.devinhouse.labsky.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import tech.devinhouse.labsky.dtos.PassageiroResponseDTO;
import tech.devinhouse.labsky.models.Passageiro;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassageiroMapper {
    List<PassageiroResponseDTO> map(List<Passageiro> passageiro);
}
