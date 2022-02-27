package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.entity.CompanyKeyResultHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CompanyKeyResultMapper.class, HistoricalCompanyKeyResultMapper.class})
public interface CompanyKeyResultHistoryMapper {
    CompanyKeyResultHistoryDto companyKeyResultHistoryToDto(CompanyKeyResultHistory companyKeyResultHistory);
}
