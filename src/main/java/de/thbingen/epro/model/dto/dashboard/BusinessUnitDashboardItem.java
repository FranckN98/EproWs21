package de.thbingen.epro.model.dto.dashboard;

import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;

import java.util.Collection;

public class BusinessUnitDashboardItem {

    private BusinessUnitObjectiveDto businessUnitObjective;
    private Collection<BusinessUnitKeyResultDto> businessUnitKeyResults;

    public BusinessUnitDashboardItem() {
    }

    public BusinessUnitDashboardItem(BusinessUnitObjectiveDto businessUnitObjective, Collection<BusinessUnitKeyResultDto> businessUnitKeyResults) {
        this.businessUnitObjective = businessUnitObjective;
        this.businessUnitKeyResults = businessUnitKeyResults;
    }

    public BusinessUnitObjectiveDto getBusinessUnitObjective() {
        return businessUnitObjective;
    }

    public void setBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjective) {
        this.businessUnitObjective = businessUnitObjective;
    }

    public Collection<BusinessUnitKeyResultDto> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public void setBusinessUnitKeyResults(Collection<BusinessUnitKeyResultDto> businessUnitKeyResults) {
        this.businessUnitKeyResults = businessUnitKeyResults;
    }
}
