package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.HistoricalBusinessUnitKeyResult;
import de.thbingen.epro.model.dto.HistoricalBusinessUnitKeyResultDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistoricalBusinessUnitKeyResultMapper {

    HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResultToDto(HistoricalBusinessUnitKeyResult historicalBusinessUnitKeyResult);
}
