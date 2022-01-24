package de.thbingen.epro.service;

import de.thbingen.epro.model.business.BusinessUnit;
import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusinessUnitKeyResultHistoryService {

    private final BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository;
    private final BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper;

    public BusinessUnitKeyResultHistoryService(BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository, BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper) {
        this.businessUnitKeyResultHistoryRepository = businessUnitKeyResultHistoryRepository;
        this.businessUnitKeyResultHistoryMapper = businessUnitKeyResultHistoryMapper;
    }

    public List<BusinessUnitKeyResultHistoryDto> findAll() {
        List<BusinessUnitKeyResultHistory> businessUnitKeyResultHistory = businessUnitKeyResultHistoryRepository.findAll();
        return businessUnitKeyResultHistoryMapper.businessUnitKeyResultHistoryToDtos(businessUnitKeyResultHistory);
    }

    public Optional<BusinessUnitKeyResultHistoryDto> findById(Long id) {
        Optional<BusinessUnitKeyResultHistory> businessUnitKeyResultHistory = businessUnitKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(businessUnitKeyResultHistoryMapper::businessUnitKeyResultHistoryToDto);
    }
}
