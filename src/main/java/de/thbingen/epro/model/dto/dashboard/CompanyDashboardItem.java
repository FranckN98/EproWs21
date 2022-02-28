package de.thbingen.epro.model.dto.dashboard;

import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;

import java.util.Collection;

public class CompanyDashboardItem {

    private CompanyObjectiveDto companyObjective;
    private Collection<CompanyKeyResultDto> companyKeyResult;

    public CompanyDashboardItem() {
    }

    public CompanyDashboardItem(CompanyObjectiveDto companyObjective, Collection<CompanyKeyResultDto> companyKeyResult) {
        this.companyObjective = companyObjective;
        this.companyKeyResult = companyKeyResult;
    }

    public CompanyObjectiveDto getCompanyObjective() {
        return companyObjective;
    }

    public void setCompanyObjective(CompanyObjectiveDto companyObjective) {
        this.companyObjective = companyObjective;
    }

    public Collection<CompanyKeyResultDto> getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(Collection<CompanyKeyResultDto> companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }
}
