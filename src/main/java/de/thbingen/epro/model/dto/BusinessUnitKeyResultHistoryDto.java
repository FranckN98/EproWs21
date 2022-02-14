package de.thbingen.epro.model.dto;

import java.time.OffsetDateTime;

public class BusinessUnitKeyResultHistoryDto {

    private Long id;
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