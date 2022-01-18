package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import de.thbingen.epro.model.business.CompanyKeyResult;

import java.util.Date;
public class CompanyKeyResultDto {

    private Long id;
    private String name;
    private Integer currentValue;
    private Integer goalValue;
    private Integer confidenceLevel;
    private Integer achievement;
    private String comment;
    private Date timestamp;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public CompanyObjectiveDto getCompanyObjective() {
        return companyObjective;
    }

    public void setCompanyObjective(CompanyObjectiveDto companyObjective) {
        this.companyObjective = companyObjective;
    }

    static CompanyKeyResultDto from(CompanyKeyResult companyKeyResult, Boolean includeObjective) {
        CompanyKeyResultDto companyKeyResultDto = new CompanyKeyResultDto();
        companyKeyResultDto.id = companyKeyResult.getId();
        companyKeyResultDto.name = companyKeyResult.getName();
        companyKeyResultDto.currentValue = companyKeyResult.getCurrentValue();
        companyKeyResultDto.goalValue = companyKeyResult.getGoalValue();
        companyKeyResultDto.confidenceLevel = companyKeyResult.getConfidenceLevel();
        companyKeyResultDto.achievement = companyKeyResult.getAchievement();
        companyKeyResultDto.comment = companyKeyResult.getComment();
        companyKeyResultDto.timestamp = companyKeyResult.getTimestamp();
        if(includeObjective) {
            companyKeyResultDto.companyObjective = CompanyObjectiveDto.from(companyKeyResult.getCompanyObjective());
        }
        return companyKeyResultDto;
    }

    static CompanyKeyResultDto from(CompanyKeyResult companyKeyResult) {
        return from(companyKeyResult, true);
    }
}
