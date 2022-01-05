package de.thbingen.epro21.model;

import javax.persistence.*;

@Entity
@Table(name="businessunitkeyresult")
public class BusinessUnitKeyResult
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name="coid",
            updatable = false
    )
    private Long id;
    @Column(
            nullable = false
    )
    private Integer achievement;
    @Column(
            nullable = false
    )
    private String name;

    public BusinessUnitKeyResult(Integer achievement, String name) {
        this.achievement = achievement;
        this.name = name;
    }

    public BusinessUnitKeyResult() {
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
}
