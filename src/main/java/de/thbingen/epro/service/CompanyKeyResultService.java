package de.thbingen.epro.service;

import de.thbingen.epro.model.business.CompanyKeyResult;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyKeyResultService {
    private final CompanyKeyResultMapper companyKeyResultMapper;
    private final CompanyKeyResultRepository companyKeyResultRepository;

    public CompanyKeyResultService(CompanyKeyResultMapper companyKeyResultMapper, CompanyKeyResultRepository companyKeyResultRepository) {
        this.companyKeyResultMapper = companyKeyResultMapper;
        this.companyKeyResultRepository = companyKeyResultRepository;
    }
    public Set<CompanyKeyResultDto> getAllCompanyKeyResults(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CompanyKeyResult> pagedResult = companyKeyResultRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return companyKeyResultMapper.companyKeyResultSetToDto(pagedResult.toSet());
        } else {
            return Collections.emptySet();
        }
    }
    public CompanyKeyResultDto saveCompanyKeyResultWithObjective(CompanyKeyResultDto companyKeyResultDto, CompanyObjectiveDto companyObjectiveDto) {
        CompanyKeyResult companyKeyResult = companyKeyResultMapper.dtoToCompanyKeyResultWithObjective(companyKeyResultDto , companyObjectiveDto);
        return companyKeyResultMapper.companyKeyResultToDto(companyKeyResultRepository.save(companyKeyResult));
    }
    public CompanyKeyResultDto saveCompanyKeyResult(CompanyKeyResultDto companyKeyResultDto) {
        CompanyKeyResult companyKeyResult = companyKeyResultMapper.dtoToCompanyKeyResult(companyKeyResultDto);
        return companyKeyResultMapper.companyKeyResultToDto(companyKeyResultRepository.save(companyKeyResult));
    }
    public void deleteById(Long id) {
        companyKeyResultRepository.deleteById(id);
    }
    public boolean existsById(Long id) {
        return companyKeyResultRepository.existsById(id);
    }

    public Optional<CompanyKeyResultDto> findById(Long id) {
        Optional<CompanyKeyResult> optional = companyKeyResultRepository.findById(id);
        return optional.map(companyKeyResultMapper::companyKeyResultToDto);
    }
}
