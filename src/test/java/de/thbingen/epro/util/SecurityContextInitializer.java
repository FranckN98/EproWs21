package de.thbingen.epro.util;

import de.thbingen.epro.model.entity.OkrUser;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.when;

public class SecurityContextInitializer {

    public static final UserRoleProvider ReadOnlyUser = new UserRoleProvider.ReadOnlyUserRoleProvider();
    public static final UserRoleProvider ViewUsersUser = new UserRoleProvider.ViewUsersUserRoleProvider();

    public static void initSecurityContextWithUser(UserRoleProvider userRoleProvider) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        OkrUser okrUser = userRoleProvider.provideUser();

        when(securityContext.getAuthentication().getPrincipal()).thenReturn(okrUser);
    }
}
