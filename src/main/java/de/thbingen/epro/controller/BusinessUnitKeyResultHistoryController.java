package de.thbingen.epro.controller;

import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;
import de.thbingen.epro.repository.BKRH;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryController {

    @Autowired
    private BKRH bkrh;
    @Autowired
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;
    @Autowired
    private CompanyKeyResultRepository companyKeyResultRepository;

    @GetMapping
    public List<BusinessUnitKeyResultHistory> getAll() {
        List<BusinessUnitKeyResultHistory> results = bkrh.findAll();
        results.forEach(item -> {
            long buoId = item.getBusinessUnitKeyResultHistoricalDto().getBusinessUnitObjectiveId();
            long companyKeyResultId = item.getBusinessUnitKeyResultHistoricalDto().getCompanyKeyResultRef();
            BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(
                    item.getBusinessUnitKeyResultHistoricalDto().getId(),
                    item.getBusinessUnitKeyResultHistoricalDto().getName(),
                    item.getBusinessUnitKeyResultHistoricalDto().getCurrentValue(),
                    item.getBusinessUnitKeyResultHistoricalDto().getGoalValue(),
                    item.getBusinessUnitKeyResultHistoricalDto().getConfidenceLevel(),
                    item.getBusinessUnitKeyResultHistoricalDto().getAchievement(),
                    item.getBusinessUnitKeyResultHistoricalDto().getComment(),
                    item.getBusinessUnitKeyResultHistoricalDto().getTimestamp(),
                    businessUnitObjectiveRepository.getById(buoId),
                    companyKeyResultRepository.getById(companyKeyResultId),
                    null
            );
            item.setBusinessUnitKeyResultHistorical(businessUnitKeyResult);
        });
        return results;
    }
}
