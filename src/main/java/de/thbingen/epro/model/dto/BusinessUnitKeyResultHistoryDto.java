package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessUnitKeyResultHistoryDto {
    private Long id;
    private String name;
    private float currentValue;
    private float goalValue;
    private float confidenceLevel;
    private Integer achievement;
    private String comment;
    private OffsetDateTime timestamp;
    private Long businessUnitObjectiveId;
    private Long companyKeyResultRef;

    public BusinessUnitKeyResultHistoryDto() {
    }

    public BusinessUnitKeyResultHistoryDto(Long id, String name, float currentValue, float goalValue, float confidenceLevel, Integer achievement, String comment, OffsetDateTime timestamp, Long businessUnitObjectiveId, Long companyKeyResultRef) {
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

    public float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public float getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(float goalValue) {
        this.goalValue = goalValue;
    }

    public float getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(float confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public Integer getAchievement() {
        return achievement;
    }

    public void setAchievement(Integer achievement) {
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