package de.thbingen.epro.controller;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.CompanyKeyResultHistoryAssembler;
import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultHistoryDto;
import de.thbingen.epro.model.dto.CompanyKeyResultHistoryDto;
import de.thbingen.epro.model.dto.HistoricalCompanyKeyResultDto;
import de.thbingen.epro.model.entity.*;
import de.thbingen.epro.model.mapper.CompanyKeyResultHistoryMapper;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.model.mapper.HistoricalCompanyKeyResultMapper;
import de.thbingen.epro.service.CompanyKeyResultHistoryService;
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
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompanyKeyResultHistoryController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                CompanyKeyResultHistoryController.class,
                                CompanyKeyResultHistoryMapper.class,
                                CompanyKeyResultHistoryAssembler.class,
                                HistoricalCompanyKeyResultMapper.class
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
public class CompanyKeyResultHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyKeyResultHistoryService companyKeyResultHistoryService;

    @Autowired
    private CompanyKeyResultHistoryAssembler companyKeyResultHistoryAssembler;

    // region GET ALL

    @Test
    @DisplayName("Get All should return all Company Key Result History with 200 - OK")
    public void getAllShouldReturnAllCompanyKeyResultHistories() throws Exception {
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "BKR1", 10, 100, 50, 25,"a comment", OffsetDateTime.now());
        HistoricalCompanyKeyResult historicalCompanyKeyResult = new HistoricalCompanyKeyResult();
        Set<CompanyKeyResultHistoryDto> companyKeyResultHistoryDtos = Stream.of(
                new CompanyKeyResultHistory(1L, OffsetDateTime.now(), companyKeyResult, historicalCompanyKeyResult),
                new CompanyKeyResultHistory(2L, OffsetDateTime.now(), companyKeyResult, historicalCompanyKeyResult)
        ).map(companyKeyResultHistoryAssembler::toModel).collect(Collectors.toSet());


        when(companyKeyResultHistoryService.findAll(Pageable.ofSize(10))).thenReturn(new PageImpl<>(new ArrayList<>(companyKeyResultHistoryDtos)));

        mockMvc.perform(get("/companyKeyResultHistory").accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.companyKeyResultHistoryList").exists())
                .andExpect(jsonPath("$._embedded.companyKeyResultHistoryList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companyKeyResultHistoryList[*]._links").exists())
                .andExpect(jsonPath("$._embedded.companyKeyResultHistoryList[0]._links.self.href", endsWith("/companyKeyResultHistory/1")))
                .andExpect(jsonPath("$._embedded.companyKeyResultHistoryList[1]._links.self.href", endsWith("/companyKeyResultHistory/2")))
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResultHistory")))
                .andReturn();
    }
    // endregion
    // region GET with id

    @Test
    @DisplayName("Get With ID should Return a single Business Unit Key result History with 200 - OK")
    public void getWithIdShouldReturnSingleBusinessUnitKeyResultHistory() throws Exception {
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "BKR1", 10, 100, 50, 25,"a comment", OffsetDateTime.now());
        HistoricalCompanyKeyResult historicalCompanyKeyResult = new HistoricalCompanyKeyResult();
        when(companyKeyResultHistoryService.findById(1L)).thenReturn(Optional.of(companyKeyResultHistoryAssembler.toModel(new CompanyKeyResultHistory(1L, OffsetDateTime.now(), companyKeyResult, historicalCompanyKeyResult))));

        mockMvc.perform(get("/companyKeyResultHistory/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links").exists())
                .andExpect(jsonPath("$._links.self.href", endsWith("/companyKeyResultHistory/1")));
    }

    // endregion
}
