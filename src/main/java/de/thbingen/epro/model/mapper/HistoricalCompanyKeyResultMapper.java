package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.HistoricalCompanyKeyResultDto;
import de.thbingen.epro.model.entity.HistoricalCompanyKeyResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoricalCompanyKeyResultMapper {

    HistoricalCompanyKeyResultDto historicalCompanyKeyResultToDto(HistoricalCompanyKeyResult historicalCompanyKeyResult);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "companyObjectiveId", ignore = true)
    HistoricalCompanyKeyResult dtoToHistoricalCompanyKeyResult(HistoricalCompanyKeyResultDto historicalCompanyKeyResultDto);
}
