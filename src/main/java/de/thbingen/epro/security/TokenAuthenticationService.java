package de.thbingen.epro.security;

import com.google.common.collect.ImmutableMap;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenAuthenticationService implements UserAuthenticationService {


    private final TokenService tokenService;
    private final OkrUserRepository okrUserRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenAuthenticationService(TokenService tokenService, OkrUserRepository okrUserRepository,
                                      PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.okrUserRepository = okrUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<String> login(final String username, final String password) {
        return okrUserRepository
                .findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
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
