package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.CompanyKeyResultHistoryAssembler;
import de.thbingen.epro.model.business.CompanyKeyResultHistory;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.repository.CompanyKeyResultHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyKeyResultHistoryService {

    private final CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository;
    private final CompanyKeyResultHistoryAssembler companyKeyResultHistoryAssembler;

    public CompanyKeyResultHistoryService(CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository, CompanyKeyResultHistoryAssembler companyKeyResultHistoryAssembler) {
        this.companyKeyResultHistoryRepository = companyKeyResultHistoryRepository;
        this.companyKeyResultHistoryAssembler = companyKeyResultHistoryAssembler;
    }

    public Page<CompanyKeyResultHistoryDto> findAll(
            int pageNo,
            int pageSize,
            String sortBy
    ) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CompanyKeyResultHistory> pagedResult = companyKeyResultHistoryRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.map(companyKeyResultHistoryAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Optional<CompanyKeyResultHistoryDto> findById(Long id) {
        Optional<CompanyKeyResultHistory> businessUnitKeyResultHistory = companyKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(companyKeyResultHistoryAssembler::toModel);
    }
}
