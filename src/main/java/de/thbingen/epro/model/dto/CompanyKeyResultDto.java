package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "companyKeyResults", itemRelation = "companyKeyResult")
public class CompanyKeyResultDto extends RepresentationModel<CompanyKeyResultDto> {

    @NotBlank
    private String name;
    private Integer currentValue;
    @Min(value = 0, message = "Negative goal Values are not allowed")
    private Integer goalValue;
    @Min(value = 0, message = "You shouldn't be negatively confident")
    @Max(value = 100, message = "Don't bee too overconfident")
    private Integer confidenceLevel;
    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Key Result")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Key Result")
    private Integer achievement;
    @NotBlank
    private String comment;
    private OffsetDateTime timestamp;

    public CompanyKeyResultDto() {
    }

    public CompanyKeyResultDto(String name, Integer currentValue, Integer goalValue, Integer confidenceLevel, Integer achievement, String comment, OffsetDateTime timestamp) {
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.comment = comment;
        this.timestamp = timestamp;
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
}
