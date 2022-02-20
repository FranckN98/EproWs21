package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.OkrUserAssembler;
import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OkrUserService {

    private final OkrUserRepository okrUserRepository;
    private final OkrUserMapper okrUserMapper;
    private final OkrUserAssembler assembler;

    public OkrUserService(OkrUserRepository OkrUserRepository, OkrUserMapper OkrUserMapper, OkrUserAssembler assembler) {
        this.okrUserRepository = OkrUserRepository;
        this.okrUserMapper = OkrUserMapper;
        this.assembler = assembler;
    }

    public Page<OkrUserDto> findAll(Pageable pageable) {
        Page<OkrUser> pagedResult = okrUserRepository.findAll(pageable);

        if(pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    public Optional<OkrUserDto> findById(Long id) {
        Optional<OkrUser> okrUser = okrUserRepository.findById(id);
        return okrUser.map(assembler::toModel);
    }

    public Page<OkrUserDto> findAllByBusinessUnitId(Long businessUnitId, Pageable pageable) {
        Page<OkrUser> pagedResult = okrUserRepository.findAllByBusinessUnitId(businessUnitId, pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        } else {
            return Page.empty();
        }
    }

    public OkrUserDto saveOkrUser(OkrUserDto OkrUserDto) {
        OkrUser okrUser = okrUserMapper.dtoToOkrUser(OkrUserDto);
        return assembler.toModel(okrUserRepository.save(okrUser));
    }

    public boolean existsById(Long id) {
        return okrUserRepository.existsById(id);
    }

    public void deleteById(Long id) {
        okrUserRepository.deleteById(id);
    }

}
