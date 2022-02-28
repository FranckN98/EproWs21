package de.thbingen.epro.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class BusinessUnitKeyResultPostDto {

    @NotBlank
    private String name;
    private Float currentValue;
    @Min(value = 1, message = "Goal Value must be at least 1")
    private Float goalValue;
    @Min(value = 0, message = "You shouldn't be negatively confident")
    @Max(value = 100, message = "Don't be too overconfident")
    private Float confidenceLevel;
    @NotBlank
    private String comment;
    private final OffsetDateTime timestamp = OffsetDateTime.now();


    public BusinessUnitKeyResultPostDto() {
    }

    public BusinessUnitKeyResultPostDto(String name, float currentValue, float goalValue, float confidenceLevel, String comment) {
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
