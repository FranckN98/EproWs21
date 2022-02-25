package de.thbingen.epro.security;

import com.google.common.collect.ImmutableMap;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {

    @Autowired
    TokenService tokenService;

    @Autowired
    OkrUserRepository okrUserRepository;

    @Override
    public Optional<String> login(final String username, final String password) {
        return okrUserRepository
                .findByUsername(username)
                .filter(user -> Objects.equals(password, user.getPassword()))
                .map(user -> tokenService.newToken(ImmutableMap.of("username", username)));
    }

    @Override
    public Optional<OkrUser> findByToken(final String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(okrUserRepository::findByUsername);
    }

    @Override
    public void logout(final OkrUser okrUser) {
    }

}
