package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.OkrUserRepository;
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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.thbingen.epro.util.SecurityContextInitializer.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {BusinessUnitService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {BusinessUnitService.class, BusinessUnitMapper.class, BusinessUnitAssembler.class}
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class BusinessUnitServiceTest {

    @Autowired
    private BusinessUnitService businessUnitService;

    @MockBean
    private static BusinessUnitRepository repository;

    @MockBean
    private OkrUserRepository okrUserRepository;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Autowired
    private BusinessUnitMapper businessUnitMapper;

    // region findAll

    @Test
    void findAllWithNoRelationsShouldReturnOnlySelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        List<BusinessUnit> businessUnits = List.of(
                new BusinessUnit(1L, "BU1", null, null),
                new BusinessUnit(2L, "BU2", null, null)
        );

        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(businessUnits, pageable, businessUnits.size()));

        Page<BusinessUnitDto> businessUnitDtoPage = businessUnitService.findAll(pageable);

        assertFalse(businessUnitDtoPage.isEmpty());
        List<BusinessUnitDto> businessUnitDtos = businessUnitDtoPage.getContent();
        assertEquals("BU1", businessUnitDtos.get(0).getName());
        assertEquals("BU2", businessUnitDtos.get(1).getName());
        assertEquals("/businessUnits/1", businessUnitDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnits/2", businessUnitDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findAllWithRelationToBUOsShouldReturnSelfLinkAndLinkToBUOs() {
        initSecurityContextWithUser(ReadOnlyUser);

        Set<BusinessUnitObjective> businessUnitObjectives = Set.of(
                new BusinessUnitObjective(1L, 0f, "TestName", LocalDate.now(), LocalDate.now())
        );
        List<BusinessUnit> businessUnits = List.of(
                new BusinessUnit(1L, "BU1", businessUnitObjectives, null),
                new BusinessUnit(2L, "BU2", null, null)
        );

        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(businessUnits, pageable, businessUnits.size()));

        Page<BusinessUnitDto> businessUnitDtoPage = businessUnitService.findAll(pageable);

        assertFalse(businessUnitDtoPage.isEmpty());
        List<BusinessUnitDto> businessUnitDtos = businessUnitDtoPage.getContent();
        assertEquals("BU1", businessUnitDtos.get(0).getName());
        assertEquals("BU2", businessUnitDtos.get(1).getName());
        assertEquals("/businessUnits/1", businessUnitDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());

        assertTrue(businessUnitDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals(
                "/businessUnits/1/objectives",
                businessUnitDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString()
        );
        assertEquals("/businessUnits/2", businessUnitDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitDtos.get(1).getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).isEmpty());
    }

    @Test
    void findAllShouldReturnEmptyPageIfNoBUsExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<BusinessUnitDto> returned = businessUnitService.findAll(Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    // endregion

    // region findById

    @Test
    void findByIdWithNoRelationsShouldReturnOnlySelfLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnit businessUnit = new BusinessUnit(1L, "Test", null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitDto businessUnitDto = returned.get();
        assertEquals("Test", businessUnitDto.getName());
        assertEquals("/businessUnits/1", businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findByIdWithBUOsShouldReturnSelfLinkAndLinkToBUOs() {
        initSecurityContextWithUser(ReadOnlyUser);

        Set<BusinessUnitObjective> businessUnitObjectives = Set.of(
                new BusinessUnitObjective(1L, 0.1f, "Test", LocalDate.now(), LocalDate.now())
        );
        BusinessUnit businessUnit = new BusinessUnit(1L, "Test", businessUnitObjectives, null);
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitDto businessUnitDto = returned.get();
        assertEquals("Test", businessUnitDto.getName());
        assertEquals("/businessUnits/1", businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals(
                "/businessUnits/1/objectives",
                businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString()
        );
    }

    @Test
    void findByIdWithBUOsAndUsersShouldNotReturnLinkToUsersWithReadOnlyUser() {
        initSecurityContextWithUser(ReadOnlyUser);

        Set<BusinessUnitObjective> businessUnitObjectives = Set.of(
                new BusinessUnitObjective(1L, 0.1f, "Test", LocalDate.now(), LocalDate.now())
        );
        Set<OkrUser> okrUsers = Set.of(
                new OkrUser()
        );
        BusinessUnit businessUnit = new BusinessUnit(1L, "Test", businessUnitObjectives, okrUsers);
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitDto businessUnitDto = returned.get();
        assertEquals("Test", businessUnitDto.getName());
        assertEquals("/businessUnits/1", businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals(
                "/businessUnits/1/objectives",
                businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString()
        );
        assertTrue(businessUnitDto.getLink(getCollectionLinkRelationFor(OkrUserDto.class)).isEmpty());
    }

    @Test
    void findByIdWithBUOsAndUsersShouldReturnLinkToUsersWithView_UsersUser() {
        initSecurityContextWithUser(ViewUsersUser);

        Set<BusinessUnitObjective> businessUnitObjectives = Set.of(
                new BusinessUnitObjective(1L, 0.1f, "Test", LocalDate.now(), LocalDate.now())
        );
        Set<OkrUser> okrUsers = Set.of(
                new OkrUser()
        );
        BusinessUnit businessUnit = new BusinessUnit(1L, "Test", businessUnitObjectives, okrUsers);
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitDto businessUnitDto = returned.get();
        assertEquals("Test", businessUnitDto.getName());
        assertEquals("/businessUnits/1", businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals(
                "/businessUnits/1/objectives",
                businessUnitDto.getLink(getCollectionLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString()
        );
        assertTrue(businessUnitDto.getLink(getCollectionLinkRelationFor(OkrUserDto.class)).isPresent());
        assertEquals(
                "/businessUnits/1/users",
                businessUnitDto.getLink(getCollectionLinkRelationFor(OkrUserDto.class)).get().toUri().toString()
        );
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfNoUserExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isEmpty());
    }

    // endregion

    // region update

    @Test
    void updateShouldThrowNotFoundExceptionIfNoBusinessUnitExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitDto updater = new BusinessUnitDto("Updater");
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> businessUnitService.updateBusinessUnit(1L, updater));
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnit businessUnit = new BusinessUnit(1L, "Old name");
        BusinessUnitDto updater = new BusinessUnitDto("Updater");
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));
        businessUnitMapper.updateBusinessUnitFromDto(updater, businessUnit);
        when(repository.save(any(BusinessUnit.class))).thenReturn(businessUnit);

        BusinessUnitDto updated = businessUnitService.updateBusinessUnit(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    // endregion

    // region insert

    @Test
    void insertBusinessUnitShouldInsertNewBusinessUnit() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitDto toBeInserted = new BusinessUnitDto("Test Name");
        BusinessUnit inserted = new BusinessUnit(1L, "Test Name");
        when(repository.save(any(BusinessUnit.class))).thenReturn(inserted);

        BusinessUnitDto returned = businessUnitService.insertBusinessUnit(toBeInserted);

        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/businessUnits/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    // endregion

    // region existsById

    @Test
    void existsByIdShouldReturnTrueIfBusinessUnitExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(businessUnitService.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfNoBusinessUnitExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(businessUnitService.existsById(1L));
    }

    // endregion

    // region deleteById

    @Test
    // No way to unit test without testing the already tested framework to be honest
    void deleteByIdReturnsNothing() {
        doNothing().when(repository).deleteById(1L);
        assertDoesNotThrow(() -> businessUnitService.deleteById(1L));
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
