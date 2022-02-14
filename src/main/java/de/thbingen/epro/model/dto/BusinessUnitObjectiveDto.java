package de.thbingen.epro.model.dto;

public class BusinessUnitObjectiveDto {

    private Long id;

    private Integer achievement;
    private String name;

    public BusinessUnitObjectiveDto(Long id, Integer achievement, String name) {
        this.id = id;
        this.achievement = achievement;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
