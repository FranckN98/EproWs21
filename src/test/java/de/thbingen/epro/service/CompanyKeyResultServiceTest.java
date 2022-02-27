package de.thbingen.epro.service;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.*;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.CompanyKeyResultRepository;
import de.thbingen.epro.repository.CompanyObjectiveRepository;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {CompanyKeyResultService.class},
        useDefaultFilters = false,
        includeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    value = {
                           CompanyKeyResultService.class,
                           CompanyKeyResultMapper.class,
                           CompanyKeyResultAssembler.class
                    }
            )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class CompanyKeyResultServiceTest {

    @Autowired
    private CompanyKeyResultService service;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Autowired
    private CompanyKeyResultMapper companyKeyResultMapper;

    @MockBean
    CompanyKeyResultRepository repository;

    @MockBean
    BusinessUnitKeyResultRepository businessUnitKeyResultRepository;

    @MockBean
    private CompanyObjectiveRepository companyObjectiveRepository;

    // region findAll

    @Test
    void findAllShouldReturnEmptyPageIfNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<CompanyKeyResultDto> returned = service.findAllCompanyKeyResults(Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllWithOnlyCompanyObjectivesShouldReturnOnlySelf() {
        initSecurityContextWithUser(ReadOnlyUser);

        CompanyObjective rootCompanyObjective = new CompanyObjective(1L, 0, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        List<CompanyKeyResult> companyKeyResults = List.of(
                new CompanyKeyResult(1L, "COKR1", 0f, 100f, 100f, 5f, "comment", OffsetDateTime.now()),
                new CompanyKeyResult(2L, "COKR2", 0f, 100f, 100f, 5f, "comment", OffsetDateTime.now())
                );
        companyKeyResults.get(0).setCompanyObjective(rootCompanyObjective);
        companyKeyResults.get(1).setCompanyObjective(rootCompanyObjective);
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(companyKeyResults, pageable, companyKeyResults.size()));

        Page<CompanyKeyResultDto> returned = service.findAllCompanyKeyResults(pageable);

        assertFalse(returned.isEmpty());
        List<CompanyKeyResultDto> companyKeyResultDtos = returned.getContent();
        assertEquals("COKR1", companyKeyResultDtos.get(0).getName());
        assertEquals("COKR2", companyKeyResultDtos.get(1).getName());
        assertEquals("/companyKeyResults/1", companyKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/companyKeyResults/2", companyKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(companyKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).isPresent());
        assertTrue(companyKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).isPresent());
        assertEquals("/companyobjectives/1", companyKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).get().toUri().toString());
        assertEquals("/companyobjectives/1", companyKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).get().toUri().toString());
    }

    @Test
    void findAllWithHistoryShouldReturnLinkToHistory() {

        initSecurityContextWithUser(ReadOnlyUser);

        CompanyObjective rootCompanyObjective = new CompanyObjective(1L, 0, "root", LocalDate.now(), LocalDate.now().plusDays(1));
        BusinessUnitKeyResult referencedBusinessUnitKeyResult = new BusinessUnitKeyResult(1L, "referenced", 0f, 100f, 0f, "I am referenced", OffsetDateTime.now());
        List<CompanyKeyResult> companyKeyResults = List.of(
                new CompanyKeyResult(1L, "COKR1", 0f, 100f, 100f, 5f, "comment", OffsetDateTime.now()),
                new CompanyKeyResult(2L, "COKR2", 0f, 100f, 50f, 5f, "comment", OffsetDateTime.now())
        );
        companyKeyResults.get(0).setCompanyObjective(rootCompanyObjective);
        companyKeyResults.get(1).setCompanyObjective(rootCompanyObjective);
        companyKeyResults.get(0).setBusinessUnitKeyResults(Set.of(referencedBusinessUnitKeyResult));
        companyKeyResults.get(1).setBusinessUnitKeyResults(Set.of(referencedBusinessUnitKeyResult));
        CompanyKeyResultHistory history = new CompanyKeyResultHistory(
                1L,
                OffsetDateTime.now(),
                companyKeyResults.get(0),
                new HistoricalCompanyKeyResult(1L, "Test", 0, 100, 100, 0, "comment", OffsetDateTime.now(), 1L)
        );
        companyKeyResults.get(0).setCompanyKeyResultHistories(Set.of(history));
        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(companyKeyResults, pageable, companyKeyResults.size()));

        Page<CompanyKeyResultDto> returned = service.findAllCompanyKeyResults(pageable);

        assertFalse(returned.isEmpty());
        List<CompanyKeyResultDto> companyKeyResultDtos = returned.getContent();
        assertEquals("COKR1", companyKeyResultDtos.get(0).getName());
        assertEquals("COKR2", companyKeyResultDtos.get(1).getName());
        assertEquals("/companyKeyResults/1", companyKeyResultDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/companyKeyResults/2", companyKeyResultDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(companyKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).isPresent());
        assertTrue(companyKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).isPresent());
        assertEquals("/companyobjectives/1", companyKeyResultDtos.get(0).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).get().toUri().toString());
        assertEquals("/companyobjectives/1", companyKeyResultDtos.get(1).getLink(getItemLinkRelationFor(CompanyObjectiveDto.class)).get().toUri().toString());
        assertTrue(companyKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1", companyKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(BusinessUnitKeyResultDto.class)).get().toUri().toString());
        assertTrue(companyKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(CompanyKeyResultHistoryDto.class)).isPresent());
        assertFalse(companyKeyResultDtos.get(1).getLink(getCollectionLinkRelationFor(CompanyKeyResultHistoryDto.class)).isPresent());
        assertEquals("/companyKeyResultHistory", companyKeyResultDtos.get(0).getLink(getCollectionLinkRelationFor(CompanyKeyResultHistoryDto.class)).get().toUri().toString());
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
