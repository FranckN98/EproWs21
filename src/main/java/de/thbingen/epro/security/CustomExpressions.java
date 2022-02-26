package de.thbingen.epro.security;


import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomExpressions {

    private final OkrUserRepository okrUserRepository;

    public CustomExpressions(OkrUserRepository okrUserRepository) {
        this.okrUserRepository = okrUserRepository;
    }

    public boolean belongsToBusinessUnit(long targetBuId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (!opt.isPresent())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();
        return targetBuId == okrUser.getBusinessUnit().getId();
    }

    public boolean isSameUser(long targetUserId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (!opt.isPresent())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();
        return targetUserId == okrUser.getId();
    }

}
