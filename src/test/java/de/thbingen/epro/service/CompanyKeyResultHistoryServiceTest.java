package de.thbingen.epro.service;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.CompanyKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.entity.CompanyKeyResult;
import de.thbingen.epro.model.entity.CompanyKeyResultHistory;
import de.thbingen.epro.model.entity.HistoricalCompanyKeyResult;
import de.thbingen.epro.model.mapper.CompanyKeyResultHistoryMapper;
import de.thbingen.epro.model.mapper.HistoricalCompanyKeyResultMapper;
import de.thbingen.epro.repository.CompanyKeyResultHistoryRepository;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {CompanyKeyResultHistoryService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                CompanyKeyResultHistoryService.class,
                                CompanyKeyResultHistoryMapper.class,
                                CompanyKeyResultHistoryAssembler.class,
                                HistoricalCompanyKeyResultMapper.class
                        }
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class CompanyKeyResultHistoryServiceTest {

    @Autowired
    private CompanyKeyResultHistoryService service;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Autowired
    private CompanyKeyResultHistoryMapper companyKeyResultHistoryMapper;

    @MockBean
    private CompanyKeyResultHistoryRepository repository;

    //region findAll
    @Test
    void findAllReturnsEmptyPageWhenNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<CompanyKeyResultHistoryDto> returned = service.findAll(Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllShouldReturnNonEmptyPageWhenEntriesExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        CompanyKeyResultHistory businessUnitKeyResultHistory = new CompanyKeyResultHistory(
                1L,
                OffsetDateTime.now(),
                new CompanyKeyResult(1L, "Name", 10f, 100f, 0f, 100f, "comment", OffsetDateTime.now()),
                new HistoricalCompanyKeyResult(
                        1L,
                        "oldName",
                        0,
                        100,
                        100,
                        0,
                        "created",
                        OffsetDateTime.now(),
                        1L
                )
        );
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(businessUnitKeyResultHistory), pageable, 1));

        Page<CompanyKeyResultHistoryDto> returned = service.findAll(pageable);

        assertTrue(returned.hasContent());
        CompanyKeyResultHistoryDto returnedHistory = returned.getContent().get(0);
        assertEquals("/companyKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
    }

    //endregion

    //region findById

    @Test
    void findByIdShouldReturnEmptyOptionalIfNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<CompanyKeyResultHistoryDto> returned = service.findById(1L);

        assertTrue(returned.isEmpty());
    }

    @Test
    void findByIdShouldReturnItemIfExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        CompanyKeyResultHistory businessUnitKeyResultHistory = new CompanyKeyResultHistory(
                1L,
                OffsetDateTime.now(),
                new CompanyKeyResult(1L, "Name", 10f, 100f, 0f, 100f, "comment", OffsetDateTime.now()),
                new HistoricalCompanyKeyResult(
                        1L,
                        "oldName",
                        0,
                        100,
                        100,
                        0,
                        "created",
                        OffsetDateTime.now(),
                        1L
                )
        );
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnitKeyResultHistory));


        Optional<CompanyKeyResultHistoryDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
        CompanyKeyResultHistoryDto returnedHistory = returned.get();
        assertEquals("/companyKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
    }

    // endregion

    // region findAllByBusinessUnitKeyResultId

    @Test
    void findAllByCompanyKeyResultIdReturnsEmptyPageWhenNoneExist() {
        when(repository.findAllByCompanyKeyResultIdOrderByChangeTimeStampDesc(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<CompanyKeyResultHistoryDto> returned = service.findAllByCompanyKeyResultId(1L, Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllByBusinessUnitKeyResultIdShouldReturnNonEmptyPageWhenEntriesExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        CompanyKeyResultHistory businessUnitKeyResultHistory = new CompanyKeyResultHistory(
                1L,
                OffsetDateTime.now(),
                new CompanyKeyResult(1L, "Name", 10f, 100f, 0f, 100f, "comment", OffsetDateTime.now()),
                new HistoricalCompanyKeyResult(
                        1L,
                        "oldName",
                        0,
                        100,
                        100,
                        0,
                        "created",
                        OffsetDateTime.now(),
                        1L
                )
        );
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAllByCompanyKeyResultIdOrderByChangeTimeStampDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(businessUnitKeyResultHistory), pageable, 1));

        Page<CompanyKeyResultHistoryDto> returned = service.findAllByCompanyKeyResultId(1L, pageable);

        assertTrue(returned.hasContent());
        CompanyKeyResultHistoryDto returnedHistory = returned.getContent().get(0);
        assertEquals("/companyKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).isPresent());
        assertEquals("/companyKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(CompanyKeyResultDto.class)).get().toUri().toString());
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
