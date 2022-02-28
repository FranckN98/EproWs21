package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.repository.PrivilegeRepository;
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

@WebMvcTest(controllers = {PrivilegeService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {PrivilegeService.class, PrivilegeMapper.class, PrivilegeAssembler.class}
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class PrivilegeServiceTest {

    @Autowired
    private PrivilegeService service;

    @MockBean
    private PrivilegeRepository repository;

    @Autowired
    private PrivilegeMapper mapper;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Test
    void findAllWithNoRelationShouldReturnOnlySelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<Privilege> privileges = List.of(
                new Privilege(1L, "P1"),
                new Privilege(1L, "P2")
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(privileges, pageable, privileges.size()));
        Page<PrivilegeDto> privilegeDtoPage = service.findAll(pageable);

        assertFalse(privilegeDtoPage.isEmpty());
        List<PrivilegeDto> privilegeDtos = privilegeDtoPage.getContent();
        assertEquals("P1", privilegeDtos.get(0).getName());
        assertEquals("P2", privilegeDtos.get(1).getName());
        assertEquals("/privileges/1", privilegeDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/privileges/1", privilegeDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findByIdReturnsSelfLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        Privilege privilege = new Privilege(1L, "P1");

        when(repository.findById(1L)).thenReturn(Optional.of(privilege));
        Optional<PrivilegeDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
        assertEquals("P1", returned.get().getName());
        assertEquals("/privileges/1", returned.get().getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        Privilege privilege = new Privilege(1L, "Old Name");
        PrivilegeDto updater = new PrivilegeDto("Updater");
        when(repository.getById(1L)).thenReturn(privilege);
        mapper.updatePrivilegeFromDto(updater, privilege);
        when(repository.save(any(Privilege.class))).thenReturn(privilege);

        PrivilegeDto updated = service.updatePrivilege(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    @Test
    void insertPrivilegeShouldInsertNewPrivilege() {
        initSecurityContextWithUser(ReadOnlyUser);

        PrivilegeDto toBeInserted = new PrivilegeDto("Test Name");
        Privilege inserted = new Privilege(1L, "Test Name");
        when(repository.save(any(Privilege.class))).thenReturn(inserted);

        PrivilegeDto returned = service.insertPrivilege(toBeInserted);

        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/privileges/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    // region existsById

    @Test
    void existsByIdShouldReturnTrueIfPrivilegeExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfPrivilegeDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

    @Test
    void findAllByRoleIdShouldReturnAllItsPrivileges() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<Privilege> privileges = List.of(
                new Privilege(1L, "P1"),
                new Privilege(1L, "P2")
        );
        Role role = new Role(1L, "R1");
        role.setPrivileges(new HashSet<>(privileges));
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByRoles_Id(1L, pageable)).thenReturn(new PageImpl<>(privileges));
        Page<PrivilegeDto> returned = service.findAllByRoleId(1L, pageable);

        assertFalse(returned.isEmpty());
        List<PrivilegeDto> privilegeDtos = returned.getContent();
        assertEquals("P1", privilegeDtos.get(0).getName());
        assertEquals("P2", privilegeDtos.get(1).getName());
        assertEquals("/privileges/1", privilegeDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/privileges/1", privilegeDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());

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
