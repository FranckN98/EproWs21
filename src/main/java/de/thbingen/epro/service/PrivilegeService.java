package de.thbingen.epro.service;

import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;
    private final PrivilegeAssembler assembler;

    public PrivilegeService(PrivilegeRepository privilegeRepository, PrivilegeMapper privilegeMapper, PrivilegeAssembler assembler) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeMapper = privilegeMapper;
        this.assembler = assembler;
    }

    public Page<PrivilegeDto> findAll(Pageable pageable) {
        Page<Privilege> pagedResult = privilegeRepository.findAll(pageable);

        if (pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    public Optional<PrivilegeDto> findById(Long id) {
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        return privilege.map(assembler::toModel);
    }

    public PrivilegeDto updatePrivilege(Long id, PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeMapper.dtoToPrivilege(privilegeDto);
        privilege.setId(id);
        return assembler.toModel(privilegeRepository.save(privilege));
    }

    public PrivilegeDto insertPrivilege(PrivilegeDto privilegeDto) {
        return updatePrivilege(null, privilegeDto);
    }

    public boolean existsById(Long id) {
        return privilegeRepository.existsById(id);
    }

    public void deleteById(Long id) {
        privilegeRepository.deleteById(id);
    }
}
