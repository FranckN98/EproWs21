package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.DashboardItem;
import de.thbingen.epro.model.dto.dashboard.BusinessUnitDashboardItem;
import de.thbingen.epro.model.dto.dashboard.CompanyDashboardItem;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.entity.CompanyObjective;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final CompanyObjectiveRepository companyObjectiveRepository;
    private final CompanyKeyResultRepository companyKeyResultRepository;
    private final BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    private final BusinessUnitKeyResultRepository businessUnitKeyResultRepository;

    private final CompanyObjectiveAssembler companyObjectiveAssembler;
    private final CompanyKeyResultAssembler companyKeyResultAssembler;
    private final BusinessUnitObjectiveAssembler businessUnitObjectiveAssembler;
    private final BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler;


    public DashboardService(CompanyObjectiveRepository companyObjectiveRepository, CompanyKeyResultRepository companyKeyResultRepository, BusinessUnitObjectiveRepository businessUnitObjectiveRepository, BusinessUnitKeyResultRepository businessUnitKeyResultRepository, CompanyObjectiveAssembler companyObjectiveAssembler, CompanyKeyResultAssembler companyKeyResultAssembler, BusinessUnitObjectiveAssembler businessUnitObjectiveAssembler, BusinessUnitKeyResultAssembler businessUnitKeyResultAssembler) {
        this.companyObjectiveRepository = companyObjectiveRepository;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.businessUnitObjectiveRepository = businessUnitObjectiveRepository;
        this.businessUnitKeyResultRepository = businessUnitKeyResultRepository;
        this.companyObjectiveAssembler = companyObjectiveAssembler;
        this.companyKeyResultAssembler = companyKeyResultAssembler;
        this.businessUnitObjectiveAssembler = businessUnitObjectiveAssembler;
        this.businessUnitKeyResultAssembler = businessUnitKeyResultAssembler;
    }

    public List<DashboardItem> getDashboardItems() {
        LocalDate currentDate = LocalDate.now();

        List<DashboardItem> dashboardItems = new ArrayList<>();

        List<CompanyObjective> companyObjectives = companyObjectiveRepository.findAllByStartDateBeforeAndEndDateAfter(currentDate, currentDate);
        for (CompanyObjective objective :
                companyObjectives) {

            DashboardItem dashboardItem = new DashboardItem();

            dashboardItem.setCompanyDashboardItem((new CompanyDashboardItem(
                    companyObjectiveAssembler.toModel(objective),
                    companyKeyResultAssembler.toCollectionModel(companyKeyResultRepository.findAllByCompanyObjectiveId(objective.getId())).getContent()
            )));

            List<BusinessUnitDashboardItem> businessUnitDashboardItems = new ArrayList<>();
            List<BusinessUnitObjective> businessUnitObjectives = businessUnitObjectiveRepository.findAllByStartDateBeforeAndEndDateAfter(currentDate, currentDate);
            for (BusinessUnitObjective bu :
                    businessUnitObjectives) {
                List<BusinessUnitKeyResult> businessUnitKeyResults = businessUnitKeyResultRepository.findAllByBusinessUnitObjectiveId(bu.getId());
                BusinessUnitDashboardItem businessUnitDashboardItem = new BusinessUnitDashboardItem();
                businessUnitDashboardItem.setBusinessUnitObjective(businessUnitObjectiveAssembler.toModel(bu));
                businessUnitDashboardItem.setBusinessUnitKeyResults(businessUnitKeyResultAssembler.toCollectionModel(businessUnitKeyResults).getContent());
                businessUnitDashboardItems.add(businessUnitDashboardItem);
            }
            dashboardItem.setBusinessUnitDashboardItems(businessUnitDashboardItems);
            dashboardItems.add(dashboardItem);
        }
        return dashboardItems;
    }
}
