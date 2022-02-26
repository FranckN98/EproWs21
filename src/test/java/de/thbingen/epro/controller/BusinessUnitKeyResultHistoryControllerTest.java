package de.thbingen.epro.controller;

import de.thbingen.epro.model.assembler.BusinessUnitKeyResultHistoryAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.entity.BusinessUnitKeyResult;
import de.thbingen.epro.model.entity.BusinessUnitKeyResultHistory;
import de.thbingen.epro.model.entity.HistoricalBusinessUnitKeyResult;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultHistoryMapper;
import de.thbingen.epro.service.BusinessUnitKeyResultHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BusinessUnitKeyResultHistoryController.class)
public class BusinessUnitKeyResultHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BusinessUnitKeyResultHistoryService businessUnitKeyResultHistoryService;

    private BusinessUnitKeyResultHistoryMapper businessUnitKeyResultHistoryMapper;
    @MockBean
    private BusinessUnitKeyResultHistoryAssembler businessUnitKeyResultHistoryAssembler;

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Business Unit Key Result History with 200 - OK")
    public void getAllShouldReturnAllBusinessUnitKeyResultHistories() throws Exception {
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "BKR1", 10, 100, 50, "a comment", OffsetDateTime.now());
        HistoricalBusinessUnitKeyResult businessUnitKeyResultHistorical = new HistoricalBusinessUnitKeyResult();
        Set<BusinessUnitKeyResultHistoryDto> businessUnitKeyResultHistoryDtos = Stream.of(
                new BusinessUnitKeyResultHistory(1L, businessUnitKeyResult, OffsetDateTime.now(), businessUnitKeyResultHistorical),
                new BusinessUnitKeyResultHistory(2L, businessUnitKeyResult, OffsetDateTime.now(), businessUnitKeyResultHistorical)
        ).map(businessUnitKeyResultHistoryAssembler::toModel).collect(Collectors.toSet());


        when(businessUnitKeyResultHistoryService.findAll(Pageable.ofSize(10))).thenReturn(new PageImpl<>(new ArrayList<>(businessUnitKeyResultHistoryDtos)));

        mockMvc.perform(get("/businessUnitKeyResultHistory").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResultHistory").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResultHistory", hasSize(2)))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResultHistory[*]._links").exists())
                .andExpect(jsonPath("$._embedded.businessUnitKeyResultHistory[0]._links.self.href", endsWith("/businessUnitKeyResultHistory/1")))
                .andExpect(jsonPath("$._embedded.businessUnitKeyResultHistory[1]._links.self.href", endsWith("/businessUnitKeyResultHistory/2")))
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
