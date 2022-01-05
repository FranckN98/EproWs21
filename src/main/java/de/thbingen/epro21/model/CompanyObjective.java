package de.thbingen.epro21.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CompanyObjective")
public class CompanyObjective {

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
    @OneToMany(
            targetEntity = CompanyKeyResult.class,
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name="coid",
            referencedColumnName = "coid"
    )
    private List<CompanyKeyResult> keyResultList = new ArrayList<>();



    public CompanyObjective(Integer achievement, String name)
    {
        this.achievement = achievement;
        this.name = name;
    }
    public CompanyObjective() {
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
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
    public List<CompanyKeyResult> getKeyResultList() {
        return keyResultList;
    }

}
