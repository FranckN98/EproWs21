package de.thbingen.epro.service;

import de.thbingen.epro.model.business.OKRUser;
import de.thbingen.epro.model.dto.OKRUserDto;
import de.thbingen.epro.model.mapper.OKRUserMapper;
import de.thbingen.epro.repository.OKRUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OKRUserService {

    private final OKRUserRepository okrUserRepository;
    private final OKRUserMapper okrUserMapper;

    public OKRUserService(OKRUserRepository okrUserRepository, OKRUserMapper okrUserMapper) {
        this.okrUserRepository = okrUserRepository;
        this.okrUserMapper = okrUserMapper;
    }

    public List<OKRUserDto> findAll() {
        List<OKRUser> okrUsers = okrUserRepository.findAll();
        return okrUserMapper.okrUserListToOKRUserList(okrUsers);
    }

    public Optional<OKRUserDto> findById(Long id) {
        Optional<OKRUser> okrUser = okrUserRepository.findById(id);
        return okrUser.map(okrUserMapper::okrUserToDto);
    }

    public OKRUserDto saveOKRUser(OKRUserDto okrUserDto) {
        OKRUser okrUser = okrUserMapper.dtoToOKRUser(okrUserDto);
        return okrUserMapper.okrUserToDto(okrUserRepository.save(okrUser));
    }

    public boolean existsById(Long id) {
        return okrUserRepository.existsById(id);
    }

    public void deleteById(Long id) {
        okrUserRepository.deleteById(id);
    }

}
