package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoricalBusinessUnitKeyResultDto {

    // Currently no validation needed, as users can't POST history
    private String name;
    private float currentValue;
    private float goalValue;
    private float confidenceLevel;
    private Integer achievement;
    private String comment;
    private OffsetDateTime timestamp;

    public HistoricalBusinessUnitKeyResultDto() {
    }

    public HistoricalBusinessUnitKeyResultDto(String name, float currentValue, float goalValue, float confidenceLevel, Integer achievement, String comment, OffsetDateTime timestamp) {
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
