package de.thbingen.epro.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Relation(collectionRelation = "companyObjectives", itemRelation = "companyObjective")
public class CompanyObjectiveDto extends RepresentationModel<CompanyObjectiveDto> {

    @Min(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    @Max(value = 0, message = "Achievement must be 0 when creating a new Company Objective")
    private Float achievement = 0f;
    private String name;
    private LocalDate startDate;
    @Future
    private LocalDate endDate;

    public CompanyObjectiveDto() {
    }

    public CompanyObjectiveDto(Float achievement, String name, LocalDate startDate, LocalDate endDate) {
        this.achievement = achievement;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Float getAchievement() {
        return achievement;
    }

    public void setAchievement(Float achievement) {
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
