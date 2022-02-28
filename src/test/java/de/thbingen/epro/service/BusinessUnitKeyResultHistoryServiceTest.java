package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.entity.HistoricalBusinessUnitKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultHistoryRepository;
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

@WebMvcTest(controllers = {BusinessUnitService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                BusinessUnitKeyResultHistoryService.class,
                                BusinessUnitKeyResultHistoryMapper.class,
                                BusinessUnitKeyResultHistoryAssembler.class
                        }
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class BusinessUnitKeyResultHistoryServiceTest {

    @Autowired
    private BusinessUnitKeyResultHistoryService service;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @MockBean
    private BusinessUnitKeyResultHistoryRepository repository;

    // region findAll

    @Test
    void findAllReturnsEmptyPageWhenNoneExist() {
        when(repository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<BusinessUnitKeyResultHistoryDto> returned = service.findAll(Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllShouldReturnNonEmptyPageWhenEntriesExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitKeyResultHistory businessUnitKeyResultHistory = new BusinessUnitKeyResultHistory(
                1L,
                new BusinessUnitKeyResult(1L, "Name", 10f, 100f, 100f, "comment", OffsetDateTime.now()),
                OffsetDateTime.now(),
                new HistoricalBusinessUnitKeyResult(
                        1L,
                        "oldName",
                        0f,
                        100f,
                        100f,
                        0f,
                        "created",
                        OffsetDateTime.now(),
                        1L,
                        null
                )
        );
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(businessUnitKeyResultHistory), pageable, 1));

        Page<BusinessUnitKeyResultHistoryDto> returned = service.findAll(pageable);

        assertTrue(returned.hasContent());
        BusinessUnitKeyResultHistoryDto returnedHistory = returned.getContent().get(0);
        assertEquals("/businessUnitKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).get().toUri().toString());
    }

    // endregion

    // region findById

    @Test
    void findByIdShouldReturnEmptyOptionalIfNoneExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<BusinessUnitKeyResultHistoryDto> returned = service.findById(1L);

        assertTrue(returned.isEmpty());
    }

    @Test
    void findByIdShouldReturnItemIfExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitKeyResultHistory businessUnitKeyResultHistory = new BusinessUnitKeyResultHistory(
                1L,
                new BusinessUnitKeyResult(1L, "Name", 10f, 100f, 100f, "comment", OffsetDateTime.now()),
                OffsetDateTime.now(),
                new HistoricalBusinessUnitKeyResult(
                        1L,
                        "oldName",
                        0f,
                        100f,
                        100f,
                        0f,
                        "created",
                        OffsetDateTime.now(),
                        1L,
                        null
                )
        );
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnitKeyResultHistory));


        Optional<BusinessUnitKeyResultHistoryDto> returned = service.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitKeyResultHistoryDto returnedHistory = returned.get();
        assertEquals("/businessUnitKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).get().toUri().toString());
    }

    // endregion

    // region findAllByBusinessUnitKeyResultId


    @Test
    void findAllByBusinessUnitKeyResultIdReturnsEmptyPageWhenNoneExist() {
        when(repository.findAllByCurrentBusinessUnitKeyResultIdOrderByChangeTimeStampDesc(anyLong(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<BusinessUnitKeyResultHistoryDto> returned = service.findAllByBusinessUnitKeyResultId(1L, Pageable.ofSize(10));

        assertTrue(returned.isEmpty());
    }

    @Test
    void findAllByBusinessUnitKeyResultIdShouldReturnNonEmptyPageWhenEntriesExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        BusinessUnitKeyResultHistory businessUnitKeyResultHistory = new BusinessUnitKeyResultHistory(
                1L,
                new BusinessUnitKeyResult(1L, "Name", 10f, 100f, 100f, "comment", OffsetDateTime.now()),
                OffsetDateTime.now(),
                new HistoricalBusinessUnitKeyResult(
                        1L,
                        "oldName",
                        0f,
                        100f,
                        100f,
                        0f,
                        "created",
                        OffsetDateTime.now(),
                        1L,
                        null
                )
        );
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAllByCurrentBusinessUnitKeyResultIdOrderByChangeTimeStampDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(businessUnitKeyResultHistory), pageable, 1));

        Page<BusinessUnitKeyResultHistoryDto> returned = service.findAllByBusinessUnitKeyResultId(1L, pageable);

        assertTrue(returned.hasContent());
        BusinessUnitKeyResultHistoryDto returnedHistory = returned.getContent().get(0);
        assertEquals("/businessUnitKeyResultHistory/1", returnedHistory.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertTrue(returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).isPresent());
        assertEquals("/businessUnitKeyResults/1", returnedHistory.getLink(getItemLinkRelationFor(BusinessUnitKeyResultDto.class)).get().toUri().toString());
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
