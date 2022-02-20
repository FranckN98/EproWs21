package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.business.CompanyObjective;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyObjectiveService {

    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final CompanyObjectiveMapper companyObjectiveMapper;
    private final CompanyObjectiveAssembler companyObjectiveAssembler;

    public CompanyObjectiveService(CompanyObjectiveRepository companyObjectiveRepository, CompanyObjectiveMapper companyObjectiveMapper, CompanyObjectiveAssembler companyObjectiveAssembler) {
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.companyObjectiveMapper = companyObjectiveMapper;
        this.companyObjectiveAssembler = companyObjectiveAssembler;
    }

    public Page<CompanyObjectiveDto> getAllCompanyObjectives(Pageable pageable) {
        Page<CompanyObjective> pagedResult = companyObjectiveRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(companyObjectiveAssembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public CompanyObjectiveDto saveCompanyObjective(CompanyObjectiveDto companyObjectiveDto) {
        CompanyObjective companyObjective = companyObjectiveMapper.dtoToCompanyObjective(companyObjectiveDto);
        return companyObjectiveAssembler.toModel(companyObjectiveRepository.save(companyObjective));
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
