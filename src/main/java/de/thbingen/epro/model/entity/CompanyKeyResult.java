package de.thbingen.epro.model.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class CompanyKeyResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float currentValue;

    @Column(nullable = false)
    private Float goalValue;

    @Column(nullable = false)
    private Float confidenceLevel;

    @Column(nullable = false, insertable = false, updatable = false)
    private Float achievement;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_objective_id")
    private CompanyObjective companyObjective;

    @OneToMany(mappedBy = "companyKeyResult", targetEntity = BusinessUnitObjective.class, cascade = CascadeType.ALL)
    private Set<BusinessUnitObjective> businessUnitObjectives = new HashSet<>();

    @OneToMany(mappedBy = "companyKeyResult", targetEntity = CompanyKeyResultHistory.class, cascade = CascadeType.ALL)
    private Set<CompanyKeyResultHistory> companyKeyResultHistories = new HashSet<>();

    @OneToMany(mappedBy = "companyKeyResult", targetEntity = BusinessUnitKeyResult.class, cascade = CascadeType.ALL)
    private Set<BusinessUnitKeyResult> businessUnitKeyResults = new HashSet<>();

    public CompanyKeyResult() {
    }

    public CompanyKeyResult(Float goalValue, Float confidenceLevel, Float achievement, String name, String comment, OffsetDateTime timestamp) {
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.name = name;
        this.comment = comment;
        this.timestamp = timestamp;
    }


    public CompanyKeyResult(Long id, String name, Float currentValue, Float goalValue, Float achievement, Float confidenceLevel, String comment, OffsetDateTime timestamp) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.comment = comment;
        this.timestamp = timestamp;
        this.companyObjective = companyObjective;
        this.businessUnitObjectives = businessUnitObjectives;
        this.companyKeyResultHistories = companyKeyResultHistories;
        this.businessUnitKeyResults = businessUnitKeyResults;
    }

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

    public Float getAchievement() {
        return achievement;
    }

    public void setAchievement(Float achievement) {
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

    public CompanyObjective getCompanyObjective() {
        return companyObjective;
    }

    public void setCompanyObjective(CompanyObjective companyObjective) {
        this.companyObjective = companyObjective;
    }

    public Set<BusinessUnitObjective> getBusinessUnitObjectives() {
        return businessUnitObjectives;
    }

    public void setBusinessUnitObjectives(Set<BusinessUnitObjective> businessUnitObjectives) {
        this.businessUnitObjectives = businessUnitObjectives;
    }

    public Set<CompanyKeyResultHistory> getCompanyKeyResultHistories() {
        return companyKeyResultHistories;
    }

    public void setCompanyKeyResultHistories(Set<CompanyKeyResultHistory> companyKeyResultHistories) {
        this.companyKeyResultHistories = companyKeyResultHistories;
    }

    public Set<BusinessUnitKeyResult> getBusinessUnitKeyResults() {
        return businessUnitKeyResults;
    }

    public void setBusinessUnitKeyResults(Set<BusinessUnitKeyResult> businessUnitKeyResults) {
        this.businessUnitKeyResults = businessUnitKeyResults;
    }

}
