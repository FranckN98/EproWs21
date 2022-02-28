package de.thbingen.epro.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public class BusinessUnitKeyResultUpdateDto {

    private Float currentValue;
    @Min(value = 0, message = "You shouldn't be negatively confident")
    @Max(value = 100, message = "Don't be too overconfident")
    private Float confidenceLevel;
    @NotBlank
    private String comment;
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    public BusinessUnitKeyResultUpdateDto() {
    }

    public BusinessUnitKeyResultUpdateDto(Float currentValue, Float confidenceLevel, String comment) {
        this.currentValue = currentValue;
        this.confidenceLevel = confidenceLevel;
        this.comment = comment;
    }

    public Float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Float currentValue) {
        this.currentValue = currentValue;
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
