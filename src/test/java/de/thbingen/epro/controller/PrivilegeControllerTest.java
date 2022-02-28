package de.thbingen.epro.controller;

import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.service.PrivilegeService;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
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
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static de.thbingen.epro.util.SecurityContextInitializer.ReadOnlyUser;
import static de.thbingen.epro.util.SecurityContextInitializer.initSecurityContextWithUser;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PrivilegeController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                PrivilegeController.class,
                                PrivilegeMapper.class,
                                PrivilegeAssembler.class,
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class PrivilegeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrivilegeService privilegeService;

    @Autowired
    private PrivilegeAssembler privilegeAssembler;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Nested
    class FindAllTests {
        @Test
        void findAllShouldReturnAllPrivileges() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            List<PrivilegeDto> privilegeDtoList = Stream.of(
                    new Privilege(1L, "P1"),
                    new Privilege(2L, "P2")
            ).map(privilegeAssembler::toModel).toList();

            when(privilegeService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(privilegeDtoList));

            LinkRelation privilegesCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(PrivilegeDto.class);

            mockMvc.perform(
                            get("/privileges")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded." + privilegesCollectionRelation +".length()", is(2)))
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$.page").exists());
        }

        @Test
        void findAllShouldReturnEmptyPageWhenNoneExist() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            when(privilegeService.findAll(any(Pageable.class))).thenReturn(Page.empty());

            mockMvc.perform(
                            get("/privileges")
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded").doesNotExist())
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$.page").exists());
        }
    }

    @Nested
    class FindByIdTests {

    }
}
