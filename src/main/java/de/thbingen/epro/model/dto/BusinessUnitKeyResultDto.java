package de.thbingen.epro.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "businessUnitKeyResults", itemRelation = "businessUnitKeyResult")
public class BusinessUnitKeyResultDto extends RepresentationModel<BusinessUnitKeyResultDto> {

    @NotBlank
    private String name;
    private float currentValue;
    @Min(value = 0, message = "Goal Value can't be negative")
    private float goalValue;
    @Min(value = 0, message = "You shouldn't be negatively confident")
    @Max(value = 100, message = "Don't be too overconfident")
    private float confidenceLevel;
    @Min(value = 0, message = "Achievement must be 0 when creating a new Bussines Unit KeyResult")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Bussines Unit KeyResult")
    private float achievement;
    @NotBlank
    private String comment;
    private OffsetDateTime timestamp = OffsetDateTime.now();


    public BusinessUnitKeyResultDto() {
    }

    public BusinessUnitKeyResultDto(String name, float currentValue, float goalValue, float confidenceLevel, float achievement, String comment, OffsetDateTime timestamp) {
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

    public void setAchievement(float achievement) {
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
