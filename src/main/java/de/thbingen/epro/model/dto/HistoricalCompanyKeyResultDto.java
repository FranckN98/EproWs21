package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricalCompanyKeyResultDto {
    private Long id;
    private String name;
    private Integer currentValue;
    private Integer goalValue;
    private Integer confidenceLevel;
    private Integer achievement;
    private String comment;
    private OffsetDateTime timestamp;
    // TODO: remove
    @JsonIgnore
    private CompanyObjectiveDto companyObjective;

    public HistoricalCompanyKeyResultDto() {
    }

    public HistoricalCompanyKeyResultDto(Long id, String name, Integer currentValue, Integer goalValue, Integer confidenceLevel, Integer achievement, String comment, OffsetDateTime timestamp, CompanyObjectiveDto companyObjective) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.comment = comment;
        this.timestamp = timestamp;
        this.companyObjective = companyObjective;
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

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Integer getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Integer goalValue) {
        this.goalValue = goalValue;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
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

    public CompanyObjectiveDto getCompanyObjective() {
        return companyObjective;
    }

    public void setCompanyObjective(CompanyObjectiveDto companyObjective) {
        this.companyObjective = companyObjective;
    }
}
