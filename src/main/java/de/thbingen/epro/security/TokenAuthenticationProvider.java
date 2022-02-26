package de.thbingen.epro.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    private final UserAuthenticationService auth;

    public TokenAuthenticationProvider(UserAuthenticationService auth) {
        super();
        this.auth = auth;
    }

    protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
    }

    protected UserDetails retrieveUser(final String username, final UsernamePasswordAuthenticationToken authentication) {
        final Object token = authentication.getCredentials();
        return Optional.ofNullable(token).map(String::valueOf).flatMap(auth::findByToken)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user"));
    }
}
