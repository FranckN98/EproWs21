package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.dto.*;
import de.thbingen.epro.model.entity.*;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
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
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
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
                        value = {
                                BusinessUnitKeyResultService.class,
                                BusinessUnitKeyResultMapper.class,
                                BusinessUnitKeyResultAssembler.class
                        }
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class BusinessUnitKeyResultServiceTest {

    @Autowired
    private BusinessUnitKeyResultService service;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Autowired
    private BusinessUnitKeyResultMapper businessUnitKeyResultMapper;

    @MockBean
    private BusinessUnitKeyResultRepository repository;

    @MockBean
    private CompanyKeyResultRepository companyKeyResultRepository;

    @MockBean
    private BusinessUnitObjectiveRepository businessUnitObjectiveRepository;

    // region findAll

    @Test
    void findAllShouldReturnEmptyPageIfNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<BusinessUnitKeyResultDto> returned = service.findAllBusinessUnitKeyResults(Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllWithOnlyBUObjectivesShouldReturnOnlySelfAndBusinessUnitLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, null, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, null, Collections.emptySet())
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllBusinessUnitKeyResults(pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
    }

    @Test
    void findAllWithReferenceToCompanyKeyResultShouldReturnLinkToCKR() {

        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyKeyResult referencedCompanyKeyResult = new CompanyKeyResult(1L, "referenced", 0f, 100f, 0f, 100f, "I am referenced", OffsetDateTime.now());
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet())
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllBusinessUnitKeyResults(pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
    }

    @Test
    void findAllWithHistoryShouldReturnLinkToHistory() {

        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyKeyResult referencedCompanyKeyResult = new CompanyKeyResult(1L, "referenced", 0f, 100f, 0f, 100f, "I am referenced", OffsetDateTime.now());
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet())
        );
        BusinessUnitKeyResultHistory history = new BusinessUnitKeyResultHistory(
                1L,
                businessUnitKeyResults.get(0),
                OffsetDateTime.now(),
                new HistoricalBusinessUnitKeyResult(1L, "Test", 0f, 100f, 100f, 0f, "comment", OffsetDateTime.now(), 1L, null)
        );
        businessUnitKeyResults.get(0).setBusinessUnitKeyResultHistories(Set.of(history));
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllBusinessUnitKeyResults(pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).isPresent());
        assertFalse(businessUnitKeyResultDtos.get(1).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1/history", businessUnitKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).get().toUri().toString());
    }

    // endregion

    // region findAllByBusinessUnitObjectiveId

    @Test
    void findAllByBusinessUnitObjectiveIdShouldReturnEmptyPageIfNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findAllByBusinessUnitObjectiveId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());

        Page<BusinessUnitKeyResultDto> returned = service.findAllByBusinessUnitObjectiveId(1L, Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllByBusinessUnitObjectiveIdWithOnlyBUObjectivesShouldReturnOnlySelfAndBusinessUnitLink() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, null, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, null, Collections.emptySet())
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByBusinessUnitObjectiveId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllByBusinessUnitObjectiveId(1L, pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
    }

    @Test
    void findAllByBusinessUnitObjectiveIdWithReferenceToCompanyKeyResultShouldReturnLinkToCKR() {

        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyKeyResult referencedCompanyKeyResult = new CompanyKeyResult(1L, "referenced", 0f, 100f, 0f, 100f, "I am referenced", OffsetDateTime.now());
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet())
        );
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByBusinessUnitObjectiveId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllByBusinessUnitObjectiveId(1L, pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
    }

    @Test
    void findAllByBusinessUnitObjectiveIdWithHistoryShouldReturnLinkToHistory() {

        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        CompanyKeyResult referencedCompanyKeyResult = new CompanyKeyResult(1L, "referenced", 0f, 100f, 0f, 100f, "I am referenced", OffsetDateTime.now());
        List<BusinessUnitKeyResult> businessUnitKeyResults = List.of(
                new BusinessUnitKeyResult(1L, "BUKR1", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet()),
                new BusinessUnitKeyResult(2L, "BUKR2", 0f, 100f, 50f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, referencedCompanyKeyResult, Collections.emptySet())
        );
        BusinessUnitKeyResultHistory history = new BusinessUnitKeyResultHistory(
                1L,
                businessUnitKeyResults.get(0),
                OffsetDateTime.now(),
                new HistoricalBusinessUnitKeyResult(1L, "Test", 0f, 100f, 100f, 0f, "comment", OffsetDateTime.now(), 1L, null)
        );
        businessUnitKeyResults.get(0).setBusinessUnitKeyResultHistories(Set.of(history));
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByBusinessUnitObjectiveId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(businessUnitKeyResults, pageable, businessUnitKeyResults.size()));

        Page<BusinessUnitKeyResultDto> returned = service.findAllByBusinessUnitObjectiveId(1L, pageable);

        assertFalse(returned.isEmpty());
        List<BusinessUnitKeyResultDto> businessUnitKeyResultDtos = returned.getContent();
        assertEquals("BUKR1", businessUnitKeyResultDtos.get(0).getName());
        assertEquals("BUKR2", businessUnitKeyResultDtos.get(1).getName());
        assertEquals("/businessUnitKeyResults/1", businessUnitKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/businessUnitKeyResults/2", businessUnitKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).isPresent());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertEquals("/businessUnitObjectives/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(BusinessUnitObjectiveDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertTrue(businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertEquals("/companyKeyResults/1", businessUnitKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
        assertTrue(businessUnitKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).isPresent());
        assertFalse(businessUnitKeyResultDtos.get(1).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1/history", businessUnitKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultHistoryDto.class)).get().toUri().toString());
    }

    // endregion

    // region insert

    @Test
    void insertBusinessUnitKeyResultShouldInsertNewBusinessUnit() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitObjective rootBusinessUnitObjective = new BusinessUnitObjective(1L, 0f, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        BusinessUnitKeyResultPostDto toBeInserted = new BusinessUnitKeyResultPostDto("Insert Me", 0f, 100f, 100f, "comment");
        BusinessUnitKeyResult inserted = new BusinessUnitKeyResult(1L, "Insert Me", 0f, 100f, 100f, "comment", OffsetDateTime.now(), rootBusinessUnitObjective, null, Collections.emptySet());
        when(repository.save(any(BusinessUnitKeyResult.class))).thenReturn(inserted);

        BusinessUnitKeyResultDto returned = service.insertBusinessUnitKeyResultWithObjective(toBeInserted, 1L);

        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/businessUnitKeyResults/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    // endregion

    // region update

    @Test
    void updateShouldThrowNotFoundExceptionIfNoBusinessUnitExists() {
        BusinessUnitKeyResultUpdateDto updater = new BusinessUnitKeyResultUpdateDto();
        updater.setComment("updater");
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.updateBusinessUnitKeyResult(1L, updater));
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "Old name", 0f, 100f, 100f, "comment", OffsetDateTime.now());
        BusinessUnitKeyResultUpdateDto updater = new BusinessUnitKeyResultUpdateDto();
        updater.setComment("updater");
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnitKeyResult));
        businessUnitKeyResultMapper.updateBusinessUnitKeyResultFromUpdateDto(updater, businessUnitKeyResult);
        when(repository.save(any(BusinessUnitKeyResult.class))).thenReturn(businessUnitKeyResult);

        BusinessUnitKeyResultDto updated = service.updateBusinessUnitKeyResult(1L, updater);
        assertEquals(updater.getComment(), updated.getComment());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    // endregion

    // region findById

    @Test
    void findByIdShouldReturnEmptyOptionalIfNoneExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<BusinessUnitKeyResultDto> returned = service.findById(1L);

        assertTrue(returned.isEmpty());
    }

    @Test
    void findByIdShouldReturnDtoIfExists() {
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "Old name", 0f, 100f, 100f, "comment", OffsetDateTime.now());

        when(repository.findById(anyLong())).thenReturn(Optional.of(businessUnitKeyResult));

        Optional<BusinessUnitKeyResultDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
    }

    // endregion

    // region existsById

    @Test
    void existsByIdShouldReturnTrueIfBusinessUnitExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfNoBusinessUnitExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

    // endregion

    // region deleteById

    @Test
        // No way to unit test without testing the already tested framework to be honest
    void deleteByIdReturnsNothing() {
        doNothing().when(repository).deleteById(1L);
        assertDoesNotThrow(() -> service.deleteById(1L));
    }

    // endregion

    // region referenceCompanyKeyResult

    @Test
    void referenceCompanyKeyResultShouldReturnTrueIfReferenceCouldBeMade() {
        CompanyKeyResult toBeReferenced = new CompanyKeyResult(1L, "name", 0f, 100f, 0f, 100f, "comment", OffsetDateTime.now());
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "Old name", 0f, 100f, 100f, "comment", OffsetDateTime.now());

        when(companyKeyResultRepository.findById(anyLong())).thenReturn(Optional.of(toBeReferenced));
        when(repository.findById(anyLong())).thenReturn(Optional.of(businessUnitKeyResult));

        boolean returned = service.referenceCompanyKeyResult(1L, 1L);

        assertTrue(returned);
    }

    @Test
    void referenceCompanyKeyResultShouldReturnFalseIfCompanyKeyResultDoesNotExist() {
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "Old name", 0f, 100f, 100f, "comment", OffsetDateTime.now());

        when(companyKeyResultRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(repository.findById(anyLong())).thenReturn(Optional.of(businessUnitKeyResult));

        boolean returned = service.referenceCompanyKeyResult(1L, 1L);

        assertFalse(returned);
    }

    @Test
    void referenceCompanyKeyResultShouldReturnFalseIfBusinessUnitKeyResultDoesNotExist() {
        CompanyKeyResult toBeReferenced = new CompanyKeyResult(1L, "name", 0f, 100f, 0f, 100f, "comment", OffsetDateTime.now());

        when(companyKeyResultRepository.findById(anyLong())).thenReturn(Optional.of(toBeReferenced));
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        boolean returned = service.referenceCompanyKeyResult(1L, 1L);

        assertFalse(returned);
    }

    // endregion

    // region deleteCompanyKeyResultReference


    @Test
    void deleteCompanyKeyResultShouldReturnTrueIfReferenceCouldBeDeleted() {
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "Old name", 0f, 100f, 100f, "comment", OffsetDateTime.now());

        when(repository.findById(anyLong())).thenReturn(Optional.of(businessUnitKeyResult));

        boolean returned = service.deleteCompanyKeyResultReference(1L);

        assertTrue(returned);
    }

    @Test
    void deleteCompanyKeyResultShouldReturnFalseIfBusinessUnitKeyResultDoesNotExist() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        boolean returned = service.deleteCompanyKeyResultReference(1L);

        assertFalse(returned);
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
