package de.thbingen.epro.service;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.BusinessUnitAssembler;
import de.thbingen.epro.model.dto.BusinessUnitDto;
import de.thbingen.epro.model.entity.*;
import de.thbingen.epro.model.mapper.BusinessUnitMapper;
import de.thbingen.epro.repository.BusinessUnitRepository;
import de.thbingen.epro.repository.OkrUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithReadOnlyUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
public class BusinessUnitServiceTest {

    @Autowired
    private BusinessUnitService businessUnitService;

    @MockBean
    private static BusinessUnitRepository repository;

    @MockBean
    private OkrUserRepository okrUserRepository;

    @Test
    void testFindById() {
        initSecurityContextWithReadOnlyUser();

        BusinessUnit businessUnit = new BusinessUnit(1L, "Test", null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(businessUnit));

        Optional<BusinessUnitDto> returned = businessUnitService.findById(1L);

        assertTrue(returned.isPresent());
        BusinessUnitDto businessUnitDto = returned.get();
        assertEquals("Test", businessUnitDto.getName());
        assertEquals("/businessUnits/1", businessUnitDto.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void testFindAll() {
        initSecurityContextWithReadOnlyUser();

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
    void testFindAllWithMoreLinks() {
        initSecurityContextWithReadOnlyUser();

        List<BusinessUnitObjective> businessUnitObjectives = List.of(
                new BusinessUnitObjective(1L, 0f, "TestName", LocalDate.now(), LocalDate.now())
        );
        List<BusinessUnit> businessUnits = List.of(
                new BusinessUnit(1L, "BU1", new HashSet<>(businessUnitObjectives), null),
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
        assertEquals("/businessUnits/1/objectives", businessUnitDtos.get(0).getLink("businessUnitObjectives").get().toUri().toString());
        assertEquals("/businessUnits/2", businessUnitDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }
}
