package de.thbingen.epro.security;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.business.Privilege;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class OkrUserDetailsService implements UserDetailsService {

    private final OkrUserRepository userRepository;

    public OkrUserDetailsService(OkrUserRepository okrUserRepository) {
        this.userRepository = okrUserRepository;
    }

    public UserDetails loadUserByUsername(String username) {
        OkrUser okrUser = userRepository.findBySurname(username);
        if (okrUser == null) {
            //handle issue
            throw new EntityNotFoundException("No User with this surname exists");
        }

        return new User(
                okrUser.getName(),
                okrUser.getPassword(),
                true,
                true,
                true,
                true,
                getAuthorities(okrUser.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<Privilege> privileges = role.getPrivileges();

        for (Privilege privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege.getName()));
        }

        return authorities;
    }

}
