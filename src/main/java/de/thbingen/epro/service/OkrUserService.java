package de.thbingen.epro.service;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OkrUserService {

    private final OkrUserRepository OkrUserRepository;
    private final OkrUserMapper OkrUserMapper;
    private final PasswordEncoder passwordEncoder;

    public OkrUserService(OkrUserRepository okrUserRepository, OkrUserMapper okrUserMapper, PasswordEncoder passwordEncoder) {
        this.OkrUserRepository = okrUserRepository;
        this.OkrUserMapper = okrUserMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<OkrUserDto> findAll() {
        List<OkrUser> okrUsers = OkrUserRepository.findAll();
        return OkrUserMapper.okrUserListToOkrUserDtoList(okrUsers);
    }

    public Optional<OkrUserDto> findById(Long id) {
        Optional<OkrUser> okrUser = OkrUserRepository.findById(id);
        return okrUser.map(OkrUserMapper::okrUserToDto);
    }

    public OkrUserDto saveOkrUser(OkrUserDto OkrUserDto) {
        OkrUser okrUser = OkrUserMapper.dtoToOkrUser(OkrUserDto);
        okrUser.setPassword(passwordEncoder.encode(okrUser.getPassword()));
        return OkrUserMapper.okrUserToDto(OkrUserRepository.save(okrUser));
    }

    public boolean existsById(Long id) {
        return OkrUserRepository.existsById(id);
    }

    public void deleteById(Long id) {
        OkrUserRepository.deleteById(id);
    }

}
