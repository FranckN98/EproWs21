package de.thbingen.epro.service;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.business.Role;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.OkrUserRepository;
import de.thbingen.epro.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class OkrUserService {

    private final OkrUserRepository okrUserRepository;
    private final OkrUserMapper okrUserMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public OkrUserService(OkrUserRepository okrUserRepository, OkrUserMapper okrUserMapper,
                          RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.okrUserRepository = okrUserRepository;
        this.okrUserMapper = okrUserMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<OkrUserDto> findAll() {
        List<OkrUser> okrUsers = okrUserRepository.findAll();
        return okrUserMapper.okrUserListToOkrUserDtoList(okrUsers);
    }

    public Optional<OkrUserDto> findById(Long id) {
        Optional<OkrUser> okrUser = okrUserRepository.findById(id);
        return okrUser.map(okrUserMapper::okrUserToDto);
    }

    public OkrUserPostDto saveOkrUser(OkrUserPostDto okrUserPostDto) {
        OkrUser okrUser = okrUserMapper.postDtoToOkrUser(okrUserPostDto);
        okrUser.setPassword(passwordEncoder.encode(okrUser.getPassword()));
        return okrUserMapper.okrUserToPostDto(okrUserRepository.save(okrUser));
    }

    public boolean existsById(Long id) {
        return okrUserRepository.existsById(id);
    }

    public void deleteById(Long id) {
        okrUserRepository.deleteById(id);
    }

    public RoleDto addNewRole(Long id, RoleDto roleDto) {
        Optional<OkrUser> okrUserResult = okrUserRepository.findById(id);
        if (!okrUserResult.isPresent()) {
            throw new EntityNotFoundException("No user with this id exists");
        }
        OkrUser okrUser = okrUserResult.get();
        Optional<Role> roleResult = roleRepository.findById(roleDto.getId());
        if (!roleResult.isPresent()) {
            throw new EntityNotFoundException(("No role with this id exists"));
        }
        Role role = roleResult.get();
        okrUser.setRole(role);
        okrUserRepository.save(okrUser);
        return roleDto;
    }

}
