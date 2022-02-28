package de.thbingen.epro.security;

import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.repository.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomExpressions {

    private final OkrUserRepository okrUserRepository;
    private final BusinessUnitKeyResultRepository bukrRepository;
    private final BusinessUnitObjectiveRepository buoRepository;

    public CustomExpressions(OkrUserRepository okrUserRepository, BusinessUnitKeyResultRepository businessUnitKeyResultRepository,
                             BusinessUnitObjectiveRepository businessUnitObjectiveRepository) {
        this.bukrRepository = businessUnitKeyResultRepository;
        this.okrUserRepository = okrUserRepository;
        this.buoRepository = businessUnitObjectiveRepository;
    }

    public boolean belongsToBusinessUnit(long targetBuId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (opt.isEmpty())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();
        return targetBuId == okrUser.getBusinessUnit().getId();
    }

    public boolean buKeyResultBelongsToSameBuAsUser(long bkrId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (opt.isEmpty())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();

        final Optional<BusinessUnitKeyResult> optBukr = bukrRepository.findById(bkrId);
        if (optBukr.isEmpty())
            throw new UsernameNotFoundException("Couldn't find business unit key result");
        BusinessUnitKeyResult bukr = optBukr.get();

        return bukr.getBusinessUnitObjective().getBusinessUnit().getId() == okrUser.getBusinessUnit().getId();
    }

    public boolean buObjectiveBelongsToSameBuAsUser(long buoId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (opt.isEmpty())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();

        final Optional<BusinessUnitObjective> optBuo = buoRepository.findById(buoId);
        if (optBuo.isEmpty())
            throw new UsernameNotFoundException("Couldn't find business unit objective");
        BusinessUnitObjective buo = optBuo.get();

        return buo.getBusinessUnit().getId() == okrUser.getBusinessUnit().getId();
    }

    public boolean isSameUser(long targetUserId, String username) {
        final Optional<OkrUser> opt = okrUserRepository.findByUsername(username);
        if (opt.isEmpty())
            throw new UsernameNotFoundException("Couldn't find user");
        OkrUser okrUser = opt.get();
        return targetUserId == okrUser.getId();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

