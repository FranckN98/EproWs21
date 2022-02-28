package de.thbingen.epro.service;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitKeyResultAssembler;
import de.thbingen.epro.model.assembler.BusinessUnitObjectiveAssembler;
import de.thbingen.epro.model.assembler.CompanyKeyResultAssembler;
import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.BusinessUnitKeyResultDto;
import de.thbingen.epro.model.dto.CompanyKeyResultDto;
import de.thbingen.epro.model.dto.dashboard.DashboardItem;
import de.thbingen.epro.model.entity.*;
import de.thbingen.epro.model.mapper.BusinessUnitKeyResultMapper;
import de.thbingen.epro.model.mapper.BusinessUnitObjectiveMapper;
import de.thbingen.epro.model.mapper.CompanyKeyResultMapper;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.repository.BusinessUnitKeyResultRepository;
import de.thbingen.epro.repository.BusinessUnitObjectiveRepository;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {DashboardService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                DashboardService.class,
                                CompanyObjectiveAssembler.class,
                                CompanyKeyResultAssembler.class,
                                BusinessUnitObjectiveAssembler.class,
                                BusinessUnitKeyResultAssembler.class,
                                CompanyObjectiveMapper.class,
                                CompanyKeyResultMapper.class,
                                BusinessUnitObjectiveMapper.class,
                                BusinessUnitKeyResultMapper.class
                        }
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class DashboardServiceTest {

    @Autowired
    private DashboardService service;

    @Autowired
    private CompanyKeyResultMapper companyKeyResultMapper;

    @Autowired
    private BusinessUnitKeyResultMapper businessUnitKeyResultMapper;

    @MockBean
    private CompanyObjectiveRepository coRepository;

    @MockBean
    private CompanyKeyResultRepository ckRepository;

    @MockBean
    private BusinessUnitObjectiveRepository buoRepository;

    @MockBean
    private BusinessUnitKeyResultRepository bukRepository;

    @Test
    void dashboardServiceReturnsListOfDashboardItems() {
        LocalDate date = LocalDate.now();
        BusinessUnit businessUnit = new BusinessUnit(1L, "BU1", null, null);
        BusinessUnitObjective businessUnitObjective = new BusinessUnitObjective(1L, 0f, "BUO1", businessUnit, null, date.minusDays(5), date.plusDays(5), null);
        BusinessUnitKeyResult businessUnitKeyResult = new BusinessUnitKeyResult(1L, "BKR1", 0f, 100f, 5f, "comment", OffsetDateTime.now());
        businessUnitKeyResult.setBusinessUnitObjective(businessUnitObjective);

        CompanyObjective companyObjective = new CompanyObjective(1L, 0f, "CO1", date.minusDays(5), date.plusDays(5));
        CompanyKeyResult companyKeyResult = new CompanyKeyResult(1L, "CKR1", 0f, 100f, 5f, 10f, "comment", OffsetDateTime.now());
        companyKeyResult.setCompanyObjective(companyObjective);

        when(coRepository.findAllByStartDateBeforeAndEndDateAfter(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(companyObjective));
        when(ckRepository.findAllByCompanyObjectiveId(any(Long.class))).thenReturn(List.of(companyKeyResult));
        when(buoRepository.findAllByStartDateBeforeAndEndDateAfter(any(LocalDate.class), any(LocalDate.class))).thenReturn(List.of(businessUnitObjective));
        when(bukRepository.findAllByBusinessUnitObjectiveId(any(Long.class))).thenReturn(List.of(businessUnitKeyResult));

        List<DashboardItem> dashboardItems = service.getDashboardItems();

        assertFalse(dashboardItems.isEmpty());
        assertNotNull(dashboardItems.get(0));
        assertNotNull(dashboardItems.get(0).getCompanyDashboardItem().getCompanyObjective());
        assertNotNull(dashboardItems.get(0).getCompanyDashboardItem().getCompanyKeyResult());
        assertNotNull(dashboardItems.get(0).getBusinessUnitDashboardItems().get(0).getBusinessUnitObjective());
        assertNotNull(dashboardItems.get(0).getBusinessUnitDashboardItems().get(0).getBusinessUnitKeyResults());

        CompanyKeyResultDto companyKeyResultDto = companyKeyResultMapper.companyKeyResultToDto(companyKeyResult);
        CompanyKeyResultDto includedCompanyKeyResultDto = (CompanyKeyResultDto) dashboardItems.get(0).getCompanyDashboardItem().getCompanyKeyResult().toArray()[0];

        BusinessUnitKeyResultDto businessUnitKeyResultDto = businessUnitKeyResultMapper.businessUnitKeyResultToDto(businessUnitKeyResult);
        BusinessUnitKeyResultDto includedBusinessUnitKeyResultDto = (BusinessUnitKeyResultDto) dashboardItems.get(0).getBusinessUnitDashboardItems().get(0).getBusinessUnitKeyResults().toArray()[0];

        assertEquals("CO1", dashboardItems.get(0).getCompanyDashboardItem().getCompanyObjective().getName());
        assertEquals(companyKeyResultDto.getName(), includedCompanyKeyResultDto.getName());
        assertEquals("BUO1", dashboardItems.get(0).getBusinessUnitDashboardItems().get(0).getBusinessUnitObjective().getName());
        assertEquals(businessUnitKeyResultDto.getName(), includedBusinessUnitKeyResultDto.getName());
    }

}
