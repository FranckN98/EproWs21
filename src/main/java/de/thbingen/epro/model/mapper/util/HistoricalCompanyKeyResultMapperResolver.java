package de.thbingen.epro.model.mapper.util;

import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.service.CompanyObjectiveService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HistoricalCompanyKeyResultMapperResolver {

    private final CompanyObjectiveService companyObjectiveService;

    public HistoricalCompanyKeyResultMapperResolver(CompanyObjectiveService companyObjectiveService) {
        this.companyObjectiveService = companyObjectiveService;
    }

    public CompanyObjectiveDto resolve(Long id) {
        Optional<CompanyObjectiveDto> optional = companyObjectiveService.findById(id);
        if (optional.isPresent()) {
            CompanyObjectiveDto companyObjectiveDto = optional.get();
            return companyObjectiveDto;
        }
        return null;
    }
}
