package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.CompanyKeyResult;

import java.time.OffsetDateTime;

public class CompanyKeyResultDto {

    private Long id;
    private String name;
    private Integer currentValue;
    private Integer goalValue;
    private Integer confidenceLevel;
    private Integer achievement;
    private String comment;
    private OffsetDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CompanyObjectiveDto companyObjective;

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
