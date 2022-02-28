package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;

@Relation(collectionRelation = "businessUnitKeyResultHistoryList", itemRelation = "businessUnitKeyResultHistory")
public class BusinessUnitKeyResultHistoryDto extends RepresentationModel<BusinessUnitKeyResultHistoryDto> {

    private OffsetDateTime changeTimeStamp;
    private HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResult;

    public BusinessUnitKeyResultHistoryDto() {
    }

    public BusinessUnitKeyResultHistoryDto(OffsetDateTime changeTimeStamp, HistoricalBusinessUnitKeyResultDto historicalBusinessUnitKeyResult) {
        this.changeTimeStamp = changeTimeStamp;
        this.historicalBusinessUnitKeyResult = historicalBusinessUnitKeyResult;
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