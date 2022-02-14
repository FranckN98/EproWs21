package de.thbingen.epro.service;

import de.thbingen.epro.model.business.Privilege;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;
    private final PrivilegeMapper privilegeMapper;

    public PrivilegeService(PrivilegeRepository privilegeRepository, PrivilegeMapper privilegeMapper) {
        this.privilegeRepository = privilegeRepository;
        this.privilegeMapper = privilegeMapper;
    }

    public List<PrivilegeDto> findAll() {
        List<Privilege> privilegeList = privilegeRepository.findAll();
        return privilegeMapper.privilegeListToPrivilegeDtoList(privilegeList);
    }

    public Optional<PrivilegeDto> findById(Long id) {
        Optional<Privilege> privilege = privilegeRepository.findById(id);
        return privilege.map(privilegeMapper::privilegeToDto);
    }

    public PrivilegeDto savePrivilege(PrivilegeDto privilegeDto) {
        Privilege privilege = privilegeMapper.dtoToPrivilege(privilegeDto);
        return privilegeMapper.privilegeToDto(privilegeRepository.save(privilege));
    }

    public boolean existsById(Long id) {
        return privilegeRepository.existsById(id);
    }

    public void deleteById(Long id) {
        privilegeRepository.deleteById(id);
    }

}
