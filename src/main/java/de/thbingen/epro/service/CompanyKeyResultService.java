package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyKeyResultService {
    private final CompanyKeyResultMapper companyKeyResultMapper;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final CompanyKeyResultAssembler assembler;

    public CompanyKeyResultService(CompanyKeyResultMapper companyKeyResultMapper, CompanyKeyResultRepository companyKeyResultRepository, CompanyKeyResultAssembler assembler) {
        this.companyKeyResultMapper = companyKeyResultMapper;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.assembler = assembler;
    }

    public Page<CompanyKeyResultDto> getAllCompanyKeyResults(Pageable pageable) {
        Page<CompanyKeyResult> pagedResult = companyKeyResultRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public CompanyKeyResultDto insertCompanyKeyResultWithObjective(CompanyKeyResultDto companyKeyResultDto, CompanyObjectiveDto companyObjectiveDto) {
        CompanyKeyResult companyKeyResult = companyKeyResultMapper.dtoToCompanyKeyResultWithObjective(companyKeyResultDto, companyObjectiveDto);
        return assembler.toModel(companyKeyResultRepository.save(companyKeyResult));
    }

    public CompanyKeyResultDto updateCompanyKeyResult(Long id, CompanyKeyResultDto companyKeyResultDto) {
        CompanyKeyResult companyKeyResult = companyKeyResultRepository.getById(id);
        companyKeyResultMapper.updateCompanyKeyResultFromDto(companyKeyResultDto, companyKeyResult);
        return assembler.toModel(companyKeyResultRepository.save(companyKeyResult));
    }

    public void deleteById(Long id) {
        companyKeyResultRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return companyKeyResultRepository.existsById(id);
    }

    public Optional<CompanyKeyResultDto> findById(Long id) {
        Optional<CompanyKeyResult> optional = companyKeyResultRepository.findById(id);
        return optional.map(assembler::toModel);
    }

    public Page<CompanyKeyResultDto> getAllByCompanyObjectiveId(Long id, Pageable pageable) {
        Page<CompanyKeyResult> pagedResult = companyKeyResultRepository.findAllByCompanyObjectiveId(id, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }
}
