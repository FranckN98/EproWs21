package de.thbingen.epro.service;

import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyObjectiveService {

    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final CompanyObjectiveMapper companyObjectiveMapper;

    public CompanyObjectiveService(CompanyObjectiveRepository companyObjectiveRepository, CompanyObjectiveMapper companyObjectiveMapper) {
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.companyObjectiveMapper = companyObjectiveMapper;
    }

    public List<CompanyObjectiveDto> getAllCompanyObjectives(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<CompanyObjective> pagedResult = companyObjectiveRepository.findAll(paging);

        if (pagedResult.hasContent()) {
            return companyObjectiveMapper.companyObjectiveListToDtoIncludeKeyResults(pagedResult.getContent());
        } else {
            return Collections.emptyList();
        }
    }

    public CompanyObjectiveDto saveCompanyObjective(CompanyObjectiveDto companyObjectiveDto) {
        CompanyObjective companyObjective = companyObjectiveMapper.dtoToCompanyObjective(companyObjectiveDto);
        return companyObjectiveMapper.companyObjectiveToDto(companyObjectiveRepository.save(companyObjective));
    }

    public Optional<CompanyObjectiveDto> findById(Long id) {
        Optional<CompanyObjective> optional = companyObjectiveRepository.findById(id);
        return optional.map(companyObjectiveMapper::companyObjectiveToDto);
    }

    public void deleteById(Long id) {
        companyObjectiveRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return companyObjectiveRepository.existsById(id);
    }
}
