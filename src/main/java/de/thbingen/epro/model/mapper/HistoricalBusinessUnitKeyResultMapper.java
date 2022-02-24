package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.HistoricalBusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.HistoricalBusinessUnitKeyResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoricalBusinessUnitKeyResultMapper {

    HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResultToDto(HistoricalBusinessUnitKeyResult historicalBusinessUnitKeyResult);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyKeyResultRef", ignore = true)
    @Mapping(target = "businessUnitObjectiveId", ignore = true)
    HistoricalBusinessUnitKeyResult dtoToHistoricalBusinessUnitKeyResult(HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResultDto);
}
