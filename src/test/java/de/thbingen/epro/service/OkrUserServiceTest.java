package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.OkrUserAssembler;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.OkrUserPostDto;
import de.thbingen.epro.model.dto.OkrUserUpdateDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.OkrUserRepository;
import de.thbingen.epro.repository.RoleRepository;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {OkrUserService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {OkrUserService.class, OkrUserMapper.class, OkrUserAssembler.class}
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class OkrUserServiceTest {

    @Autowired
    private OkrUserService service;

    @MockBean
    private OkrUserRepository repository;

    @Autowired
    private OkrUserMapper mapper;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Test
    void findAllWithNoRelationShouldReturnOnlySelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<OkrUser> okrUsers = List.of(
                new OkrUser(1L, "V1", "N1", "V1.N1", "password1"),
                new OkrUser(2L, "V2", "N2", "V2.N2", "password2")
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(okrUsers, pageable, okrUsers.size()));
        Page<OkrUserDto> okrUserDtoPage = service.findAll(pageable);

        assertFalse(okrUserDtoPage.isEmpty());
        List<OkrUserDto> roleDtos = okrUserDtoPage.getContent();
        assertEquals("V1", roleDtos.get(0).getName());
        assertEquals("V2", roleDtos.get(1).getName());
        assertEquals("/users/1", roleDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/users/2", roleDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findByIdReturnsSelfLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        OkrUser okrUser = new OkrUser(1L, "V1", "N1", "V1.N1", "password");

        when(repository.findById(1L)).thenReturn(Optional.of(okrUser));
        Optional<OkrUserDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
        assertEquals("V1", returned.get().getName());
        assertEquals("/users/1", returned.get().getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        OkrUser okrUser = new OkrUser(1L, "Old V1", "N1", "Old V1.N1", "password");
        OkrUserUpdateDto updater = new OkrUserUpdateDto("Updater", "N1", "Updater.N1", "password");
        when(repository.getById(1L)).thenReturn(okrUser);
        mapper.updateOkrUserFromUpdateDto(updater, okrUser);
        when(repository.save(any(OkrUser.class))).thenReturn(okrUser);

        OkrUserDto updated = service.updateOkrUser(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    @Test
    void insertUserShouldInsertNewUser() {
        initSecurityContextWithUser(ReadOnlyUser);

        OkrUserPostDto toBeInserted = new OkrUserPostDto("Test", "Name", "Test.Name", "password");
        OkrUser inserted = new OkrUser(1L, "Test", "Name", "Test.Name", "password");
        when(repository.save(any(OkrUser.class))).thenReturn(inserted);

        OkrUserDto returned = service.insertOkrUser(toBeInserted);

        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/users/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    // region existsById

    @Test
    void existsByIdShouldReturnTrueIfUserExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfUserDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

    @Test
    void findAllByBusinessUnitIdShouldReturnAllUsers() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnit businessUnit = new BusinessUnit(1L, "BU1", null, null);
        List<OkrUser> okrUsers = List.of(
                new OkrUser(1L, "V1", "N1", "V1.N1", "password1"),
                new OkrUser(2L, "V2", "N2", "V2.N2", "password2")
        );
        businessUnit.setOkrUsers(new HashSet<>(okrUsers));
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByBusinessUnitId(1L, pageable)).thenReturn(new PageImpl<>(okrUsers));
        Page<OkrUserDto> okrUserDtoPage = service.findAllByBusinessUnitId(1L, pageable);

        assertFalse(okrUserDtoPage.isEmpty());
        List<OkrUserDto> okrUserDtos = okrUserDtoPage.getContent();
        assertEquals("V1", okrUserDtos.get(0).getName());
        assertEquals("V2", okrUserDtos.get(1).getName());
        assertEquals("/users/1", okrUserDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/users/2", okrUserDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findAllWithRoleIdShouldReturnAllItsUsers() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<OkrUser> okrUsers = List.of(
                new OkrUser(1L, "V1", "N1", "V1.N1", "password1"),
                new OkrUser(2L, "V2", "N2", "V2.N2", "password2")
        );
        Role role = new Role(1L, "R1");
        okrUsers.get(0).setRole(role);
        okrUsers.get(1).setRole(role);
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByRoleId(1L, pageable)).thenReturn(new PageImpl<>(okrUsers));
        when(roleRepository.existsById(1L)).thenReturn(true);
        Page<OkrUserDto> returned = service.findAllUsersWithRole(1L, pageable);

        assertFalse(returned.isEmpty());
        List<OkrUserDto> okrUserDtos = returned.getContent();
        assertEquals("V1", okrUserDtos.get(0).getName());
        assertEquals("V2", okrUserDtos.get(1).getName());
        assertEquals("/users/1", okrUserDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/users/2", okrUserDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());

    }

    @Test
    void addingANewRole() {
        OkrUser okrUser = new OkrUser(1L, "V1", "N1", "V1.N1", "password");
        Role role = new Role(1L, "R1");
        RoleDto newRoleDto = new RoleDto("R1");
        when(repository.findById(1L)).thenReturn(Optional.of(okrUser));
        when(repository.save(any(OkrUser.class))).thenReturn(okrUser);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        service.addNewRole(1L, newRoleDto);

        assertTrue(okrUser.getRole() == role);
    }

    // endregion


    // region convenience Methods

    // Convenience Method for somewhat improved readability
    private LinkRelation getItemLinkRelationFor(Class<?> type) {
        return annotationLinkRelationProvider.getItemResourceRelFor(type);
    }

    // Convenience Method for somewhat improved readability
    private LinkRelation getCollectionLinkRelationFor(Class<?> type) {
        return annotationLinkRelationProvider.getCollectionResourceRelFor(type);
    }

    // endregion

}
