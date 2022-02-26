package de.thbingen.epro.service;

import de.thbingen.epro.model.business.Privilege;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    final RoleRepository roleRepository;
    final RoleMapper roleMapper;
    final PrivilegeRepository privilegeRepository;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.privilegeRepository = privilegeRepository;
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

    public PrivilegeDto addNewPrivilege(Long id, PrivilegeDto privilegeDto) {
        Optional<Role> roleResult = roleRepository.findById(id);
        if (!roleResult.isPresent()) {
            throw new EntityNotFoundException("No role with this id exists");
        }
        Role role = roleResult.get();
        Optional<Privilege> privilegeResult = privilegeRepository.findById(privilegeDto.getId());
        if (!privilegeResult.isPresent()) {
            throw new EntityNotFoundException("No privilege with this id exists");
        }
        Privilege privilege = privilegeResult.get();
        role.addPrivilege(privilege);
        roleRepository.save(role);
        return privilegeDto;
    }
}
