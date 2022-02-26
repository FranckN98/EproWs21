package de.thbingen.epro.service;

import de.thbingen.epro.model.business.OkrUser;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.OkrUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OkrUserService {

    private final OkrUserRepository okrUserRepository;
    private final OkrUserMapper okrUserMapper;
    private final PasswordEncoder passwordEncoder;

    public OkrUserService(OkrUserRepository okrUserRepository, OkrUserMapper okrUserMapper, PasswordEncoder passwordEncoder) {
        this.okrUserRepository = okrUserRepository;
        this.okrUserMapper = okrUserMapper;
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

}
