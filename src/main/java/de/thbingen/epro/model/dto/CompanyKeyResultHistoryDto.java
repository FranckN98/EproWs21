package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;

@Relation(collectionRelation = "companyKeyResultHistoryList", itemRelation = "companyKeyResultHistory")
public class CompanyKeyResultHistoryDto extends RepresentationModel<CompanyKeyResultHistoryDto> {

    private OffsetDateTime changeTimeStamp;
    private HistoricalCompanyKeyResultDto historicalCompanyKeyResult;

    public CompanyKeyResultHistoryDto() {
    }

    public CompanyKeyResultHistoryDto(OffsetDateTime changeTimeStamp, HistoricalCompanyKeyResultDto historicalCompanyKeyResult) {
        this.changeTimeStamp = changeTimeStamp;
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }

    public OffsetDateTime getChangeTimeStamp() {
        return changeTimeStamp;
    }

    public void setChangeTimeStamp(OffsetDateTime changeTimeStamp) {
        this.changeTimeStamp = changeTimeStamp;
    }

    public HistoricalCompanyKeyResultDto getHistoricalCompanyKeyResult() {
        return historicalCompanyKeyResult;
    }

    public void setHistoricalCompanyKeyResult(HistoricalCompanyKeyResultDto historicalCompanyKeyResult) {
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }
}
