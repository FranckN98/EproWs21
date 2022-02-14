package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.CompanyKeyResultHistory;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CompanyKeyResultMapper.class, HistoricalCompanyKeyResultMapper.class})
public interface CompanyKeyResultHistoryMapper {
    CompanyKeyResultHistoryDto companyKeyResultHistoryToDto(CompanyKeyResultHistory companyKeyResultHistory);

    CompanyKeyResultHistory dtoToCompanyKeyResultHistory(CompanyKeyResultHistoryDto companyKeyResultHistoryDto);

    List<CompanyKeyResultHistoryDto> companyKeyResultHistoryToDtos(List<CompanyKeyResultHistory> companyKeyResultHistories);
}
