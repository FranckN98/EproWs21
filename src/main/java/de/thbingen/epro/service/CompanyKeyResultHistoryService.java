package de.thbingen.epro.service;

import de.thbingen.epro.model.business.CompanyKeyResultHistory;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.mapper.CompanyKeyResultHistoryMapper;
import de.thbingen.epro.repository.CompanyKeyResultHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyKeyResultHistoryService {
    private final CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository;
    private final CompanyKeyResultHistoryMapper companyKeyResultHistoryMapper;

    public CompanyKeyResultHistoryService(CompanyKeyResultHistoryRepository companyKeyResultHistoryRepository, CompanyKeyResultHistoryMapper companyKeyResultHistoryMapper) {
        this.companyKeyResultHistoryRepository = companyKeyResultHistoryRepository;
        this.companyKeyResultHistoryMapper = companyKeyResultHistoryMapper;
    }

    public List<CompanyKeyResultHistoryDto> findAll() {
        List<CompanyKeyResultHistory> businessUnitKeyResultHistory = companyKeyResultHistoryRepository.findAll();
        return companyKeyResultHistoryMapper.companyKeyResultHistoryToDtos(businessUnitKeyResultHistory);
    }

    public Optional<CompanyKeyResultHistoryDto> findById(Long id) {
        Optional<CompanyKeyResultHistory> businessUnitKeyResultHistory = companyKeyResultHistoryRepository.findById(id);
        return businessUnitKeyResultHistory.map(companyKeyResultHistoryMapper::companyKeyResultHistoryToDto);
    }
}
