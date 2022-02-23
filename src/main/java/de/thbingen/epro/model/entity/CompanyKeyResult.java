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
    private Integer currentValue;

    @Column(nullable = false)
    private Integer goalValue;

    @Column(nullable = false)
    private Integer confidenceLevel;

    @Column(nullable = false, insertable = false, updatable = false)
    private Integer achievement;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_objective_id")
    private CompanyObjective companyObjective;

    @OneToMany(targetEntity = BusinessUnitObjective.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")
    private Set<BusinessUnitObjective> businessUnitObjectives = new HashSet<>();

    @OneToMany(mappedBy = "companyKeyResult", targetEntity = CompanyKeyResultHistory.class, cascade = CascadeType.ALL)
    private Set<CompanyKeyResultHistory> companyKeyResultHistories = new HashSet<>();

    @OneToMany(targetEntity = BusinessUnitKeyResult.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_id")
    private Set<BusinessUnitKeyResult> businessUnitKeyResults = new HashSet<>();

    public CompanyKeyResult() {
    }

    public CompanyKeyResult(Integer goalValue, Integer confidenceLevel, Integer achievement, String name, String comment, OffsetDateTime timestamp) {
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.achievement = achievement;
        this.name = name;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    /*public CompanyKeyResult(Long id, String name, Integer currentValue, Integer goalValue, Integer confidenceLevel, Integer achievement, String comment, Date timestamp, CompanyObjective companyObjective, Set<BusinessUnitObjective> businessUnitObjectives, Set<CompanyKeyResultHistory> companyKeyResultHistories, Set<BusinessUnitKeyResult> businessUnitKeyResults) {
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
    }*/

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
