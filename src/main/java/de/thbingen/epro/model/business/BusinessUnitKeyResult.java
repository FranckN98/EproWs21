package de.thbingen.epro.model.business;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class BusinessUnitKeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private float currentValue;

    @Column
    private float goalValue;

    @Column
    private float confidenceLevel;

    @Column(nullable = false, insertable = false, updatable = false)
    private Integer achievement = 0;

    @Column
    private String comment;

    @Column
    private OffsetDateTime timestamp;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "business_unit_objective_id")
    private BusinessUnitObjective businessUnitObjective;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_key_result_ref")
    private CompanyKeyResult companyKeyResult;

    @OneToMany(targetEntity = BusinessUnitKeyResultHistory.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ref_id")
    private Set<BusinessUnitKeyResultHistory> businessUnitKeyResultHistories = new HashSet<>();

    public BusinessUnitKeyResult() {
    }

    public BusinessUnitKeyResult(Long id, String name, float currentValue, float goalValue, float confidenceLevel, String comment, OffsetDateTime timestamp, BusinessUnitObjective businessUnitObjective, CompanyKeyResult companyKeyResult, Set<BusinessUnitKeyResultHistory> businessUnitKeyResultHistories) {
        this.id = id;
        this.name = name;
        this.currentValue = currentValue;
        this.goalValue = goalValue;
        this.confidenceLevel = confidenceLevel;
        this.comment = comment;
        this.timestamp = timestamp;
        this.businessUnitObjective = businessUnitObjective;
        //this.businessUnitKeyResultHistories = businessUnitKeyResultHistories;
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

    public BusinessUnitObjective getBusinessUnitObjective() {
        return businessUnitObjective;
    }

    public void setBusinessUnitObjective(BusinessUnitObjective businessUnitObjective) {
        this.businessUnitObjective = businessUnitObjective;
    }

    public Set<BusinessUnitKeyResultHistory> getBusinessUnitKeyResultHistories() {
        return businessUnitKeyResultHistories;
    }

    public void setBusinessUnitKeyResultHistories(Set<BusinessUnitKeyResultHistory> businessUnitKeyResultHistories) {
        this.businessUnitKeyResultHistories = businessUnitKeyResultHistories;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public CompanyKeyResult getCompanyKeyResult() {
        return companyKeyResult;
    }

    public void setCompanyKeyResult(CompanyKeyResult companyKeyResult) {
        this.companyKeyResult = companyKeyResult;
    }
}
