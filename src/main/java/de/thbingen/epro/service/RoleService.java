package de.thbingen.epro.service;

import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    final RoleRepository roleRepository;
    final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.roleListToRoleDtoList(roles);
    }

    public Optional<RoleDto> findById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(roleMapper::roleToDto);
    }

    public RoleDto saveRole(RoleDto roleDto) {
        Role role = roleMapper.dtoToRole(roleDto);
        return roleMapper.roleToDto(roleRepository.save(role));
    }

    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
