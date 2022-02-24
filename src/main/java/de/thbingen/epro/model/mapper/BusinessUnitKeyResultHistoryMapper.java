package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultHistoryMapper {
    BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistoryToDto(BusinessUnitKeyResultHistory businessUnitKeyResultHistory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currentBusinessUnitKeyResult", ignore = true)
    @Mapping(target = "businessUnitKeyResultHistorical", ignore = true)
    BusinessUnitKeyResultHistory dtoToBusinessUnitKeyResultHistory(BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistory);
}
