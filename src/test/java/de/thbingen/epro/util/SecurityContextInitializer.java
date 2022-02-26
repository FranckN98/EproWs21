package de.thbingen.epro.util;

import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.mockito.Mockito.when;

public class SecurityContextInitializer {

    public static void initSecurityContextWithReadOnlyUser() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Role role = new Role("Read Only");
        role.setPrivileges(Set.of(new Privilege("read")));
        OkrUser okrUser = new OkrUser();
        okrUser.setRole(role);

        when(securityContext.getAuthentication().getPrincipal()).thenReturn(okrUser);
    }
}
