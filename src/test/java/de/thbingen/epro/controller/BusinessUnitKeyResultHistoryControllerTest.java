package de.thbingen.epro.controller;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.entity.HistoricalBusinessUnitKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BusinessUnitKeyResultHistoryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                BusinessUnitKeyResultHistoryController.class,
                                BusinessUnitKeyResultHistoryMapper.class,
                                BusinessUnitKeyResultHistoryAssembler.class
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class BusinessUnitKeyResultHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;

    @Autowired
    private BusinessUnitKeyResultHistoryAssembler businessUnitKeyResultHistoryAssembler;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Business Unit Key Result History with 200 - OK")
    public void getAllShouldReturnAllBusinessUnitKeyResultHistories() throws Exception {
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now());
        HistoricalBusinessUnitKeyResult businessUnitKeyResultHistorical = new HistoricalBusinessUnitKeyResult();
        List<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistoryDtos = Stream.of(
                new BusinessUnitKeyResultHistory(1L, businessUnitKeyResult, OffsetDateTime.now(), businessUnitKeyResultHistorical),
                new BusinessUnitKeyResultHistory(2L, businessUnitKeyResult, OffsetDateTime.now(), businessUnitKeyResultHistorical)
        ).map(businessUnitKeyResultHistoryAssembler::toModel).toList();


        when(businessUnitKeyResultHistoryService.findAll(Pageable.ofSize(10)))
                .thenReturn(new PageImpl<>(businessUnitKeyResultHistoryDtos));

        String expectedLinkRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(BusinessUnitKeyResultHistoryDto.class).toString();

        mockMvc.perform(get("/businessUnitKeyResultHistory").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded." + expectedLinkRelation).exists())
                .andExpect(jsonPath("$._embedded." + expectedLinkRelation, hasSize(2)))
                .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[*]._links").exists())
                .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[0]._links.self.href", endsWith("/businessUnitKeyResultHistory/1")))
                .andExpect(jsonPath("$._embedded." + expectedLinkRelation + "[1]._links.self.href", endsWith("/businessUnitKeyResultHistory/2")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResultHistory")))
                .andReturn();
    }
    // endregion
    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Business Unit Key result History with 200 - OK")
    public void getWithIdShouldReturnSingleBusinessUnitKeyResultHistory() throws Exception {
        HistoricalBusinessUnitKeyResult businessUnitKeyResultHistorical = new HistoricalBusinessUnitKeyResult();
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now());
        when(businessUnitKeyResultHistoryService.findById(1L)).thenReturn(Optional.of(businessUnitKeyResultHistoryAssembler.toModel(new BusinessUnitKeyResultHistory(1L, businessUnitKeyResult, OffsetDateTime.now(), businessUnitKeyResultHistorical))));

        mockMvc.perform(get("/businessUnitKeyResultHistory/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/businessUnitKeyResultHistory/1")));
    }

    // endregion


}
