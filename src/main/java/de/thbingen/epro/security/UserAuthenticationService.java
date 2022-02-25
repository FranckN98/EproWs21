package de.thbingen.epro.security;

import de.thbingen.epro.model.business.OkrUser;

import java.util.Optional;

public interface UserAuthenticationService {
    Optional<String> login(String username, String password);
    Optional<OkrUser> findByToken(String token);
    void logout(OkrUser okrUser);
}
