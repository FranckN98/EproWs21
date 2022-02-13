package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.BusinessUnitKeyResult;
import de.thbingen.epro.model.business.BusinessUnitObjective;
import de.thbingen.epro.model.business.CompanyKeyResult;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BusinessUnitKeyResultDto {

    private Long id;
    private String name;
    private float currentValue;
    private float goalValue;
    private float confidenceLevel;
    private float achievement = 0;
    private String comment;
    private OffsetDateTime timestamp = OffsetDateTime.now();
    private BusinessUnitObjectiveDto businessUnitObjective;
    private CompanyKeyResultDto companyKeyResult;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistories = new HashSet<>();


    public BusinessUnitKeyResultDto() {
    }

    public BusinessUnitKeyResultDto(Long id, String name, float currentValue, float goalValue, float confidenceLevel, Integer achievement, String comment, OffsetDateTime timestamp, BusinessUnitObjectiveDto businessUnitObjective) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.comment = comment;
        this.timestamp = timestamp;
        this.businessUnitObjective = businessUnitObjective;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public float getAchievement() {
        return achievement;
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

    public BusinessUnitObjectiveDto getBusinessUnitObjective() {
        return businessUnitObjective;
    }

    public void setBusinessUnitObjective(BusinessUnitObjectiveDto businessUnitObjective) {
        this.businessUnitObjective = businessUnitObjective;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BusinessUnitKeyResultHistoryDto> getBusinessUnitKeyResultHistories() {
        return businessUnitKeyResultHistories;
    }

    public void setBusinessUnitKeyResultHistories(Set<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistories) {
        this.businessUnitKeyResultHistories = businessUnitKeyResultHistories;
    }

    public CompanyKeyResultDto getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(CompanyKeyResultDto companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }
}
