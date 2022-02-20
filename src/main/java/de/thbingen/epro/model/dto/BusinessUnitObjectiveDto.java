package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "businessUnitObjectives", itemRelation = "businessUnitObjective")
public class BusinessUnitObjectiveDto extends RepresentationModel<BusinessUnitObjectiveDto> {

    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement;
    @NotBlank
    private String name;

    private OffsetDateTime startDate = OffsetDateTime.now();

    private OffsetDateTime endDate = OffsetDateTime.now();

    public BusinessUnitObjectiveDto() {
    }

    public BusinessUnitObjectiveDto(Integer achievement, String name, OffsetDateTime startDate, OffsetDateTime endDate) {
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getAchievement() {
        return achievement;
    }

    public void setAchievement(Integer achievement) {
        this.achievement = achievement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OffsetDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(OffsetDateTime startDate) {
        this.startDate = startDate;
    }

    public OffsetDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(OffsetDateTime endDate) {
        this.endDate = endDate;
    }

}
