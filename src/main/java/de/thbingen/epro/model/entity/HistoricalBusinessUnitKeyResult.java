package de.thbingen.epro.model.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HistoricalBusinessUnitKeyResult {
    private Long id;
    private String name;
    private Float currentValue;
    private Float goalValue;
    private Float confidenceLevel;
    private Float achievement;
    private String comment;
    private OffsetDateTime timestamp;
    private Long businessUnitObjectiveId;
    private Long companyKeyResultRef;

    public HistoricalBusinessUnitKeyResult() {
    }

    public HistoricalBusinessUnitKeyResult(Long id, String name, float currentValue, float goalValue, float confidenceLevel, Float achievement, String comment, OffsetDateTime timestamp, Long businessUnitObjectiveId, Long companyKeyResultRef) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.comment = comment;
        this.timestamp = timestamp;
        this.businessUnitObjectiveId = businessUnitObjectiveId;
        this.companyKeyResultRef = companyKeyResultRef;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Float currentValue) {
        this.currentValue = currentValue;
    }

    public Float getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Float goalValue) {
        this.goalValue = goalValue;
    }

    public Float getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Float confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Float getAchievement() {
        return achievement;
    }

    public void setAchievement(Float achievement) {
        this.achievement = achievement;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getBusinessUnitObjectiveId() {
        return businessUnitObjectiveId;
    }

    public void setBusinessUnitObjectiveId(Long businessUnitObjectiveId) {
        this.businessUnitObjectiveId = businessUnitObjectiveId;
    }

    public Long getCompanyKeyResultRef() {
        return companyKeyResultRef;
    }

    public void setCompanyKeyResultRef(Long companyKeyResultRef) {
        this.companyKeyResultRef = companyKeyResultRef;
    }
}
