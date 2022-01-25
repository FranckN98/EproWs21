package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultHistoryMapper {
    BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistoryToDto(BusinessUnitKeyResultHistory businessUnitKeyResultHistory);
    BusinessUnitKeyResultHistory dtoToBusinessUnitKeyResultHistory(BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistory);

    List<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistoryToDtos(List<BusinessUnitKeyResultHistory> businessUnitKeyResultHistories);

    // TODO: check removal(?)
    public BusinessUnitKeyResultDto BusinessUnitKeyResultToDto(BusinessUnitKeyResult businessUnitKeyResult);
}
