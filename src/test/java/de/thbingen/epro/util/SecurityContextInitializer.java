package de.thbingen.epro.util;

import de.thbingen.epro.model.entity.OkrUser;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.when;

public class SecurityContextInitializer {

    public static final UserProvider ReadOnlyUser = new UserProvider.ReadOnlyUserProvider();
    public static final UserProvider ViewUsersUser = new UserProvider.ViewUsersUserProvider();

    public static void initSecurityContextWithUser(UserProvider userProvider) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        OkrUser okrUser = userProvider.provideUser();

        when(securityContext.getAuthentication().getPrincipal()).thenReturn(okrUser);
    }
}
