package de.thbingen.epro.model.mapper;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessUnitKeyResultHistoryMapper {
    BusinessUnitKeyResultHistoryDto businessUnitKeyResultHistoryToDto(BusinessUnitKeyResultHistory businessUnitKeyResultHistory);
}
