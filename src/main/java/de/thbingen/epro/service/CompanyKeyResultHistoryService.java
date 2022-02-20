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
    private final CompanyKeyResultHistoryAssembler assembler;

    public CompanyKeyResultHistoryService(CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository, CompanyKeyResultHistoryAssembler assembler) {
        this.companyKeyResultHistoryRepository = companyKeyResultHistoryRepository;
        this.assembler = assembler;
    }

    public Page<CompanyKeyResultHistoryDto> findAll(Pageable pageable) {
        Page<CompanyKeyResultHistory> pagedResult = companyKeyResultHistoryRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public Optional<CompanyKeyResultHistoryDto> findById(Long id) {
        Optional<CompanyKeyResultHistory> businessUnitKeyResultHistory = companyKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(assembler::toModel);
    }

    public Page<CompanyKeyResultHistoryDto> getAllByCompanyKeyResultId(Long id, Pageable pageable) {
        Page<CompanyKeyResultHistory> pagedResult = companyKeyResultHistoryRepository.findAllByCompanyKeyResultIdOrderByChangeTimeStampDesc(id, pageable);

        if(pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }
}
