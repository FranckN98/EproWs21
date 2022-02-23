package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitKeyResultHistoryService {

    private final BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository;
    private final BusinessUnitKeyResultHistoryAssembler assembler;

    public BusinessUnitKeyResultHistoryService(BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository, BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper, BusinessUnitKeyResultHistoryAssembler assembler) {
        this.businessUnitKeyResultHistoryRepository = businessUnitKeyResultHistoryRepository;
        this.assembler = assembler;
    }

    public Page<BusinessUnitKeyResultHistoryDto> findAll(Pageable pageable) {
        Page<BusinessUnitKeyResultHistory> pagedResult = businessUnitKeyResultHistoryRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Optional<BusinessUnitKeyResultHistoryDto> findById(Long id) {
        Optional<BusinessUnitKeyResultHistory> businessUnitKeyResultHistory = businessUnitKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(assembler::toModel);
    }

    public Page<BusinessUnitKeyResultHistoryDto> getAllByBusinessUnitKeyResultId(Long id, Pageable pageable) {
        Page<BusinessUnitKeyResultHistory> pagedResult = businessUnitKeyResultHistoryRepository.findAllByCurrentBusinessUnitKeyResultIdOrderByChangeTimeStampDesc(id, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }
}
