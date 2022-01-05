package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="companykeyresult")
public class CompanyKeyResult
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name="krid",
            updatable = false
    )
    private Long id;
    @Column(
            name="goalvalue",
            nullable = false
    )
    private Integer goalValue;
    @Column(
            name="confidencelevel",
            nullable = false
    )
    private Integer confidencelevel;
    @Column(
            nullable = false
    )
    private Integer achievement;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String comment;
    private Integer companyobjectivid;
    @Column(
            nullable = false
    )
    private Date timestamp;

    public CompanyKeyResult(Integer goalvalue, Integer confidencelevel, Integer achievement, String name, String comment, Integer companyobjectivid, Date timestamp) {
        this.goalValue = goalvalue;
        this.confidencelevel = confidencelevel;
        this.achievement = achievement;
        this.name = name;
        this.comment = comment;
        this.companyobjectivid = companyobjectivid;
        this.timestamp = timestamp;
    }

    public CompanyKeyResult() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGoalValue() {
        return goalValue;
    }

    public void setGoalValue(Integer goalvalue) {
        this.goalValue = goalvalue;
    }

    public Integer getConfidencelevel() {
        return confidencelevel;
    }

    public void setConfidencelevel(Integer confidencelevel) {
        this.confidencelevel = confidencelevel;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCompanyobjectivid() {
        return companyobjectivid;
    }

    public void setCompanyobjectivid(Integer companyobjectivid) {
        this.companyobjectivid = companyobjectivid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
