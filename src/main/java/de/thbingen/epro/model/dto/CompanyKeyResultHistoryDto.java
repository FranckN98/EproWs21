package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;

@Relation(collectionRelation = "companyKeyResultHistoryList", itemRelation = "companyKeyResultHistory")
public class CompanyKeyResultHistoryDto extends RepresentationModel<CompanyKeyResultHistoryDto> {
    private Long id;
    private OffsetDateTime changeTimeStamp;
    @JsonIgnore
    private CompanyKeyResultDto companyKeyResult;
    private HistoricalCompanyKeyResultDto historicalCompanyKeyResult;

    public CompanyKeyResultHistoryDto() {
    }

    public CompanyKeyResultHistoryDto(Long id, OffsetDateTime changeTimeStamp, CompanyKeyResultDto companyKeyResult, HistoricalCompanyKeyResultDto historicalCompanyKeyResult) {
        this.id = id;
        this.changeTimeStamp = changeTimeStamp;
        this.companyKeyResult = companyKeyResult;
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getChangeTimeStamp() {
        return changeTimeStamp;
    }

    public void setChangeTimeStamp(OffsetDateTime changeTimeStamp) {
        this.changeTimeStamp = changeTimeStamp;
    }

    public CompanyKeyResultDto getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(CompanyKeyResultDto companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }

    public HistoricalCompanyKeyResultDto getHistoricalCompanyKeyResult() {
        return historicalCompanyKeyResult;
    }

    public void setHistoricalCompanyKeyResult(HistoricalCompanyKeyResultDto historicalCompanyKeyResult) {
        this.historicalCompanyKeyResult = historicalCompanyKeyResult;
    }
}
