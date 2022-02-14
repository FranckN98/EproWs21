package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import de.thbingen.epro.model.business.BusinessUnitKeyResultHistory;

import java.time.OffsetDateTime;

@Relation(collectionRelation = "businessUnitKeyResultHistoryList", itemRelation = "businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryDto extends RepresentationModel<BusinessUnitKeyResultHistoryDto> {

    //TODO: Remove jsonignores in dtos!
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private BusinessUnitKeyResultDto currentBusinessUnitKeyResult;
    private OffsetDateTime changeTimeStamp;
    private HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResult;

    public BusinessUnitKeyResultHistoryDto() {
    }

    public BusinessUnitKeyResultHistoryDto(Long id, BusinessUnitKeyResultDto currentBusinessUnitKeyResult, OffsetDateTime changeTimeStamp, HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResult) {
        this.id = id;
        this.currentBusinessUnitKeyResult = currentBusinessUnitKeyResult;
        this.changeTimeStamp = changeTimeStamp;
        this.historicalBusinessUnitKeyResult = historicalBusinessUnitKeyResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessUnitKeyResultDto getCurrentBusinessUnitKeyResult() {
        return currentBusinessUnitKeyResult;
    }

    public void setCurrentBusinessUnitKeyResult(BusinessUnitKeyResultDto currentBusinessUnitKeyResult) {
        this.currentBusinessUnitKeyResult = currentBusinessUnitKeyResult;
    }

    public OffsetDateTime getChangeTimeStamp() {
        return changeTimeStamp;
    }

    public void setChangeTimeStamp(OffsetDateTime changeTimeStamp) {
        this.changeTimeStamp = changeTimeStamp;
    }

    public HistoricalBusinessUnitKeyResultDto getHistoricalBusinessUnitKeyResult() {
        return historicalBusinessUnitKeyResult;
    }

    public void setHistoricalBusinessUnitKeyResult(HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResult) {
        this.historicalBusinessUnitKeyResult = historicalBusinessUnitKeyResult;
    }


}