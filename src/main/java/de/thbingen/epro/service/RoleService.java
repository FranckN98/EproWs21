package de.thbingen.epro.service;

import de.thbingen.epro.controller.assembler.RoleAssembler;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final RoleAssembler assembler;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, RoleAssembler assembler) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.assembler = assembler;
    }

    public Page<RoleDto> findAll(Pageable pageable) {
        Page<Role> pagedResult = roleRepository.findAll(pageable);

        if(pagedResult.hasContent()) {
            return pagedResult.map(assembler::toModel);
        }
        return Page.empty();
    }

    public Optional<RoleDto> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(assembler::toModel);
    }

    public RoleDto saveRole(RoleDto roleDto) {
        Role role = roleMapper.dtoToRole(roleDto);
        return assembler.toModel(roleRepository.save(role));
    }

    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
