package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "businessUnitObjectives", itemRelation = "businessUnitObjective")
@Valid()
public class BusinessUnitObjectiveDto extends RepresentationModel<BusinessUnitObjectiveDto> {

    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Integer achievement = 0;
    private String name;

    private LocalDate startDate;
    @Future
    private LocalDate endDate;

    public BusinessUnitObjectiveDto() {
    }

    public BusinessUnitObjectiveDto(Integer achievement, String name, LocalDate startDate, LocalDate endDate) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @AssertTrue(message = "The End date must be after the startDate")
    @JsonIgnore
    public boolean isEndAfterBeginning() {
        return endDate.isAfter(startDate);
    }

}
