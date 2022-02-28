package de.thbingen.epro.util;

public enum UserLogin {
    CO_ADMIN("{\"username\": \"vor.nach1\",\"password\": \"password1\"}"),
    BU_ADMIN("{\"username\": \"vor.nach2\",\"password\": \"password2\"}"),
    READ_ONLY_USER("{\"username\": \"vor.nach3\",\"password\": \"password3\"}");

    private final String loginJson;

    UserLogin(String loginJson) {
        this.loginJson = loginJson;
    }

    public String getLoginJson() {
        return loginJson;
    }
}
