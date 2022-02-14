package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessUnitKeyResultHistoryService {

    private final BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository;
    private final BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper;
    private final BusinessUnitKeyResultHistoryAssembler businessUnitKeyResultHistoryAssembler;

    public BusinessUnitKeyResultHistoryService(BusinessUnitKeyResultHistoryRepository businessUnitKeyResultHistoryRepository, BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper, BusinessUnitKeyResultHistoryAssembler businessUnitKeyResultHistoryAssembler) {
        this.businessUnitKeyResultHistoryRepository = businessUnitKeyResultHistoryRepository;
        this.businessUnitKeyResultHistoryMapper = businessUnitKeyResultHistoryMapper;
        this.businessUnitKeyResultHistoryAssembler = businessUnitKeyResultHistoryAssembler;
    }

    public Page<BusinessUnitKeyResultHistoryDto> findAll(
            int pageNo,
            int pageSize,
            String sortBy
    ) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<BusinessUnitKeyResultHistory> pagedResult = businessUnitKeyResultHistoryRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.map(businessUnitKeyResultHistoryAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Optional<BusinessUnitKeyResultHistoryDto> findById(Long id) {
        Optional<BusinessUnitKeyResultHistory> businessUnitKeyResultHistory = businessUnitKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(businessUnitKeyResultHistoryAssembler::toModel);
    }
}
