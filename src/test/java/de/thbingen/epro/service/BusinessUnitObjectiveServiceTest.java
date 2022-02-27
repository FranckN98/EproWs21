package de.thbingen.epro.service;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitObjectiveDto;
import de.thbingen.epro.model.entity.BusinessUnit;
import de.thbingen.epro.model.entity.BusinessUnitObjective;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
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

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {BusinessUnitObjectiveService.class},
        useDefaultFilters = false,
        includeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    value = {BusinessUnitObjectiveService.class,
                            BusinessUnitObjectiveMapper.class,
                            BusinessUnitObjectiveAssembler.class
                    }
            )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class BusinessUnitObjectiveServiceTest {

    @Autowired
    private BusinessUnitObjectiveService service;

    @MockBean
    private BusinessUnitObjectiveRepository repository;

    @MockBean
    private CompanyKeyResultRepository companyKeyResultRepository;

    @MockBean
    private BusinessUnitRepository businessUnitRepository;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Autowired
    private BusinessUnitObjectiveMapper mapper;

    // region findAll

    @Test
    void findAllByBusinessUnitIdShouldReturnSelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        BusinessUnit businessUnit = new BusinessUnit(1L, "BU1", null, null);

        List<BusinessUnitObjective> businessUnitObjectives = List.of(
                new BusinessUnitObjective(1L, 100f, "BUO1", businessUnit, null, LocalDate.now(), date.plusDays(1), null),
                new BusinessUnitObjective(2L, 100f, "BUO2", businessUnit, null, LocalDate.now(), date.plusDays(1), null)
        );

        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByBusinessUnitIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                1L, date, date, pageable))
                .thenReturn(new PageImpl<>(businessUnitObjectives, pageable, businessUnitObjectives.size()));

        Page<BusinessUnitObjectiveDto> businessUnitObjectiveDto = service.findAllByBusinessUnitId(1L, pageable, date, date);

        assertFalse(businessUnitObjectiveDto.isEmpty());
        List<BusinessUnitObjectiveDto> businessUnitObjectiveDtos = businessUnitObjectiveDto.getContent();
        assertEquals("BUO1", businessUnitObjectiveDtos.get(0).getName());
        assertEquals("BUO2", businessUnitObjectiveDtos.get(1).getName());
        assertEquals("/businessUnitObjectives/1", businessUnitObjectiveDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitObjectives/2", businessUnitObjectiveDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findAllByBusinessUnitIdShouldReturnEmptyPageIfEmpty() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAllByBusinessUnitIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
                1L, date, date, pageable))
                .thenReturn(Page.empty());

        Page<BusinessUnitObjectiveDto> businessUnitObjectiveDto = service.findAllByBusinessUnitId(1L, pageable, date, date);
        assertTrue(businessUnitObjectiveDto.isEmpty());
    }

    @Test
    void getAllBusinessUnitObjectivesShouldReturnSelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        BusinessUnit businessUnit = new BusinessUnit(1L, "BU1", null, null);

        List<BusinessUnitObjective> businessUnitObjectives = List.of(
                new BusinessUnitObjective(1L, 100f, "BUO1", businessUnit, null, LocalDate.now(), date.plusDays(1), null),
                new BusinessUnitObjective(2L, 100f, "BUO2", businessUnit, null, LocalDate.now(), date.plusDays(1), null)
        );

        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByStartDateAfterAndEndDateBefore(
                 date, date, pageable))
                .thenReturn(new PageImpl<>(businessUnitObjectives, pageable, businessUnitObjectives.size()));

        Page<BusinessUnitObjectiveDto> businessUnitObjectiveDto = service.getAllBusinessUnitObjectives(pageable, date, date);

        assertFalse(businessUnitObjectiveDto.isEmpty());
        List<BusinessUnitObjectiveDto> businessUnitObjectiveDtos = businessUnitObjectiveDto.getContent();
        assertEquals("BUO1", businessUnitObjectiveDtos.get(0).getName());
        assertEquals("BUO2", businessUnitObjectiveDtos.get(1).getName());
        assertEquals("/businessUnitObjectives/1", businessUnitObjectiveDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitObjectives/2", businessUnitObjectiveDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void updateShouldThrowNullPointerExceptionIfBusinessUnitObjectiveDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjectiveDto updater = new BusinessUnitObjectiveDto(10f, "Updater", LocalDate.now(), LocalDate.now().plusDays(1));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> service.updateBusinessUnitObjective(1L, updater));
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnit businessUnit = new BusinessUnit(1L, "BU1", null, null);
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 10f, "Old Name", businessUnit, null, LocalDate.now(), LocalDate.now().plusDays(1), null);
        BusinessUnitObjectiveDto updater = new BusinessUnitObjectiveDto(10f, "Updater", LocalDate.now(), LocalDate.now().plusDays(1));

        when(repository.getById(1L)).thenReturn(businessUnitObjective);
        mapper.updateBusinessUnitObjectiveFromDto(updater, businessUnitObjective);
        when(repository.save(any(BusinessUnitObjective.class))).thenReturn(businessUnitObjective);

        BusinessUnitObjectiveDto updated = service.updateBusinessUnitObjective(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    @Test
    void existsByIdShouldReturnTrueIfBusinessUnitObjectiveExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfBusinessUnitObjectiveDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

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
