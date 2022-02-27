package de.thbingen.epro.service;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.CompanyObjectiveAssembler;
import de.thbingen.epro.model.dto.CompanyObjectiveDto;
import de.thbingen.epro.model.entity.CompanyObjective;
import de.thbingen.epro.model.mapper.CompanyObjectiveMapper;
import de.thbingen.epro.repository.*;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {CompanyObjectiveService.class},
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {CompanyObjectiveService.class,
                                CompanyObjectiveMapper.class,
                                CompanyObjectiveAssembler.class,
                        }
                )
        }
)
@Import({RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class CompanyObjectiveServiceTest {

    @Autowired
    private CompanyObjectiveService service;

    @MockBean
    private CompanyObjectiveRepository repository;

    @MockBean
    private CompanyKeyResultRepository companyKeyResultRepository;

    @Autowired
    private CompanyObjectiveMapper mapper;

    //region getAll

    @Test
    void getAllCompanyObjectivesShouldReturnSelfLinks() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        List<CompanyObjective> companyObjectives = List.of(
                new CompanyObjective(1L, 0f, "CO1", date, date.plusDays(1)),
                new CompanyObjective(1L, 0f, "CO2", date, date.plusDays(1))
        );

        Pageable pageable = Pageable.ofSize(10);

        when(repository.findAllByStartDateAfterAndEndDateBefore(date, date.plusDays(1), pageable)).thenReturn(
                new PageImpl<>(companyObjectives, pageable, companyObjectives.size()));
        Page<CompanyObjectiveDto> companyObjectiveDto = service.getAllCompanyObjectives(pageable, date, date.plusDays(1));

        assertFalse(companyObjectiveDto.isEmpty());
        List<CompanyObjectiveDto> companyObjectiveDtos = companyObjectiveDto.getContent();
        assertEquals("CO1", companyObjectiveDtos.get(0).getName());
        assertEquals("CO2", companyObjectiveDtos.get(1).getName());
        assertEquals("/companyObjectives/1", companyObjectiveDtos.get(0).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
        assertEquals("/companyObjectives/1", companyObjectiveDtos.get(1).getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void findByIdShouldReturnEmptyIfObjectiveDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertTrue(service.findById(1L).isEmpty());
    }

    @Test
    void findByIdShouldReturnSelfLinkWhenObjectiveExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        CompanyObjective companyObjective = new CompanyObjective(1L, 0f, "CO1", date, date.plusDays(1));

        when(repository.findById(1L)).thenReturn(Optional.of(companyObjective));

        Optional<CompanyObjectiveDto> companyObjectiveDto = service.findById(1L);
        assertTrue(companyObjectiveDto.isPresent());
        assertEquals("CO1", companyObjectiveDto.get().getName());
    }

    @Test
    void insertCompanyObjectiveShouldInsertNewCompanyObjective() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        CompanyObjectiveDto toBeInserted = new CompanyObjectiveDto(0f, "CO1", date, date.plusDays(1));
        CompanyObjective inserted = new CompanyObjective(1L, 0f, "CO1", date, date.plusDays(1));

        when(repository.save(any(CompanyObjective.class))).thenReturn(inserted);

        CompanyObjectiveDto returned = service.insertCompanyObjective(toBeInserted);
        assertEquals(toBeInserted.getName(), returned.getName());
        assertEquals("/companyObjectives/1", returned.getRequiredLink(IanaLinkRelations.SELF).toUri().toString());
    }

    @Test
    void updateShouldUpdateName() {
        initSecurityContextWithUser(ReadOnlyUser);

        LocalDate date = LocalDate.now();

        CompanyObjective companyObjective = new CompanyObjective(1L, 0f, "Old Name", date, date.plusDays(1));
        CompanyObjectiveDto updater = new CompanyObjectiveDto(0f, "Updater", date, date.plusDays(1));

        when(repository.getById(1L)).thenReturn(companyObjective);
        mapper.updateCompanyObjectiveFromDto(updater, companyObjective);
        when(repository.save(any(CompanyObjective.class))).thenReturn(companyObjective);

        CompanyObjectiveDto updated = service.updateCompanyObjective(1L, updater);
        assertEquals(updater.getName(), updated.getName());
        assertDoesNotThrow(() -> updated.getRequiredLink(IanaLinkRelations.SELF));
    }

    @Test
    void existsByIdShouldReturnTrueIfObjectiveExists() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(true);

        assertTrue(service.existsById(1L));
    }

    @Test
    void existsByIdShouldReturnFalseIfObjectiveDoesNotExist() {
        initSecurityContextWithUser(ReadOnlyUser);

        when(repository.existsById(1L)).thenReturn(false);

        assertFalse(service.existsById(1L));
    }

}
