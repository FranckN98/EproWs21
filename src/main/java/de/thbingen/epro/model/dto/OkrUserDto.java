package de.thbingen.epro.model.dto;


import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;

@Relation(collectionRelation = "users", itemRelation = "user")
public class OkrUserDto extends RepresentationModel<OkrUserDto> {

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private String username;

    @NotBlank
    private String password;

    public OkrUserDto() {
    }

    public OkrUserDto(String name, String surname, String username, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
