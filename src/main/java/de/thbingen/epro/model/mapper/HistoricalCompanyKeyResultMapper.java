package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.HistoricalCompanyKeyResultDto;
import de.thbingen.epro.model.entity.HistoricalCompanyKeyResult;
import de.thbingen.epro.model.mapper.util.HistoricalCompanyKeyResultMapperResolver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {HistoricalCompanyKeyResultMapperResolver.class})
public interface HistoricalCompanyKeyResultMapper {
    HistoricalCompanyKeyResultDto historicalCompanyKeyResultToDto(HistoricalCompanyKeyResult historicalCompanyKeyResult);
}
