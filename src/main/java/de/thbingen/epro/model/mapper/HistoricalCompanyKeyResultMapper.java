package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.HistoricalCompanyKeyResult;
import de.thbingen.epro.model.dto.HistoricalCompanyKeyResultDto;
import de.thbingen.epro.model.mapper.util.HistoricalCompanyKeyResultMapperResolver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HistoricalCompanyKeyResultMapperResolver.class})
public interface HistoricalCompanyKeyResultMapper {
    HistoricalCompanyKeyResultDto historicalCompanyKeyResultToDto(HistoricalCompanyKeyResult historicalCompanyKeyResult);
}
