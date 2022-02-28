package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.RoleAssembler;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {RoleService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {RoleService.class, RoleMapper.class, RoleAssembler.class}
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class RoleServiceTest {

    @Autowired
    private RoleService service;

    @MockBean
    private RoleRepository repository;

    @Autowired
    private RoleMapper mapper;

    @MockBean
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Test
    void findAllWithNoRelationShouldReturnOnlySelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<Role> roles = List.of(
                new Role(1L, "R1"),
                new Role(2L, "R2")
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(roles, pageable, roles.size()));
        Page<RoleDto> roleDtoPage = service.findAll(pageable);

        assertFalse(roleDtoPage.isEmpty());
        List<RoleDto> roleDtos = roleDtoPage.getContent();
        assertEquals("R1", roleDtos.get(0).getName());
        assertEquals("R2", roleDtos.get(1).getName());
        assertEquals("/roles/1", roleDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/roles/2", roleDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findByIdReturnsSelfLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        Role role = new Role(1L, "R1");

        when(repository.findById(1L)).thenReturn(Optional.of(role));
        Optional<RoleDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
        assertEquals("R1", returned.get().getName());
        assertEquals("/roles/1", returned.get().getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        Role role = new Role(1L, "Old Name");
        RoleDto updater = new RoleDto("Updater");
        when(repository.getById(1L)).thenReturn(role);
        mapper.updateRoleFromDto(updater, role);
        when(repository.save(any(Role.class))).thenReturn(role);

        RoleDto updated = service.updateRole(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    @Test
    void insertRoleShouldInsertNewRole() {
        initSecurityContextWithUser(ReadOnlyUser);

        RoleDto toBeInserted = new RoleDto("Test Name");
        Role inserted = new Role(1L, "Test Name");
        when(repository.save(any(Role.class))).thenReturn(inserted);

        RoleDto returned = service.insertRole(toBeInserted);

        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/roles/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    // region existsById

    @Test
    void existsByIdShouldReturnTrueIfRoleExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfRoleDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

    @Test
    void addingANewPrivilege() {
        Role role = new Role(1L, "R1");
        role.setPrivileges(new HashSet<>());
        Privilege privilege = new Privilege(1L, "P1");
        PrivilegeDto newPrivilegeDto = new PrivilegeDto("P1");
        when(repository.findById(1L)).thenReturn(Optional.of(role));
        when(repository.save(any(Role.class))).thenReturn(role);
        when(privilegeRepository.findById(1L)).thenReturn(Optional.of(privilege));
        service.addNewPrivilege(1L, newPrivilegeDto);

        assertTrue(role.getPrivileges().contains(privilege));
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
