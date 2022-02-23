package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.HistoricalBusinessUnitKeyResultDto;
import de.thbingen.epro.model.entity.HistoricalBusinessUnitKeyResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoricalBusinessUnitKeyResultMapper {

    HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResultToDto(HistoricalBusinessUnitKeyResult historicalBusinessUnitKeyResult);
}
