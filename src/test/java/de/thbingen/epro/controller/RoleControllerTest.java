package de.thbingen.epro.controller;


import de.thbingen.epro.exception.RestExceptionHandler;
import de.thbingen.epro.model.assembler.OkrUserAssembler;
import de.thbingen.epro.model.assembler.PrivilegeAssembler;
import de.thbingen.epro.model.assembler.RoleAssembler;
import de.thbingen.epro.model.dto.OkrUserDto;
import de.thbingen.epro.model.dto.PrivilegeDto;
import de.thbingen.epro.model.dto.RoleDto;
import de.thbingen.epro.model.entity.OkrUser;
import de.thbingen.epro.model.entity.Privilege;
import de.thbingen.epro.model.entity.Role;
import de.thbingen.epro.model.mapper.OkrUserMapper;
import de.thbingen.epro.model.mapper.PrivilegeMapper;
import de.thbingen.epro.model.mapper.RoleMapper;
import de.thbingen.epro.service.OkrUserService;
import de.thbingen.epro.service.PrivilegeService;
import de.thbingen.epro.service.RoleService;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.thbingen.epro.util.SecurityContextInitializer.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                RoleController.class,
                                RoleMapper.class,
                                RoleAssembler.class,
                                PrivilegeMapper.class,
                                PrivilegeAssembler.class,
                                OkrUserMapper.class,
                                OkrUserAssembler.class
                        }
                )}
)
@Import(RestExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @MockBean
    private OkrUserService okrUserService;

    @MockBean
    private PrivilegeService privilegeService;

    @Autowired
    private RoleAssembler assembler;

    @Autowired
    private PrivilegeAssembler privilegeAssembler;

    @Autowired
    private OkrUserAssembler okrUserAssembler;

    @Autowired
    private AnnotationLinkRelationProvider annotationLinkRelationProvider;

    @Nested
    class GetAllTests {
        @Test
        public void getAllShouldReturnAllRoles() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            List<RoleDto> roles = Stream.of(
                    new Role(1L, "TestRole1"),
                    new Role(2L, "TestRole2")
            ).map(assembler::toModel).collect(Collectors.toList());
            when(roleService.findAll(Pageable.ofSize(10))).thenReturn(new PageImpl<>(roles));

            LinkRelation roleCollectionRelation = annotationLinkRelationProvider.getCollectionResourceRelFor(RoleDto.class);

            mockMvc.perform(get("/roles").accept(MediaTypes.HAL_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded").exists())
                    .andExpect(jsonPath("$._embedded." + roleCollectionRelation).exists())
                    .andExpect(jsonPath("$._embedded." + roleCollectionRelation, hasSize(2)))
                    .andExpect(jsonPath("$._embedded." + roleCollectionRelation + "[*]._links").exists())
                    .andExpect(jsonPath("$._embedded." + roleCollectionRelation + "..name", everyItem(matchesRegex("TestRole\\d"))))
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links.self.href", endsWith("/roles")));
        }

        @Test
        public void getAllShouldReturnNoneIfNoneExist() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            when(roleService.findAll(Pageable.ofSize(10))).thenReturn(Page.empty());

            mockMvc.perform(get("/roles").accept(MediaTypes.HAL_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.page").exists())
                    .andExpect(jsonPath("$._embedded").doesNotExist())
                    .andExpect(jsonPath("$._links").exists())
                    .andExpect(jsonPath("$._links.self.href", endsWith("/roles")));
        }
    }

    @Nested
    class FindByIdTests {
        @Test
        public void findByIdShouldReturn_404_IfNoneExist() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            when(roleService.findById(anyLong())).thenReturn(Optional.empty());

            mockMvc.perform(get("/roles/1").accept(MediaTypes.HAL_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        public void findByIdShouldReturn_200_IfRoleExists() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            Optional<RoleDto> roleDto = Optional.of(new Role(1L, "Test")).map(assembler::toModel);

            when(roleService.findById(anyLong())).thenReturn(roleDto);

            mockMvc.perform(get("/roles/1").accept(MediaTypes.HAL_JSON))
                    .andDo(print())
                    .andExpect(jsonPath("$.name", is(roleDto.get().getName())))
                    .andExpect(jsonPath("$._links.length()", is(1)))
                    .andExpect(jsonPath("$._links", hasKey("self")));
        }
    }

    @Nested
    class AddNewTests {

        @Test
        public void addNewShouldReturn_201_OnValidBody() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            RoleDto toPost = assembler.toModel(new Role(1L, "test"));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            when(roleService.insertRole(ArgumentMatchers.any(RoleDto.class))).thenReturn(toPost);

            mockMvc.perform(
                            post("/roles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is(toPost.getName())))
                    .andExpect(jsonPath("$._links", hasKey("self")));

        }

        @Test
        public void addNewShouldReturn_400_OnBlankName() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            RoleDto toPost = assembler.toModel(new Role(1L, ""));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            when(roleService.insertRole(ArgumentMatchers.any(RoleDto.class))).thenReturn(toPost);

            mockMvc.perform(
                            post("/roles")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("Invalid JSON")))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].object", is("roleDto")))
                    .andExpect(jsonPath("$.errors[0].field", is("name")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", is("")))
                    .andExpect(jsonPath("$.errors[0].message", is("must not be blank")));

        }
    }

    @Nested
    class UpdateByIdTests {

        @Test
        public void updateByIdShouldReturn_404_WithInvalidId() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            RoleDto toPut = assembler.toModel(new Role(1L, "Test"));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPut = objectMapper.writeValueAsString(toPut);

            when(roleService.existsById(anyLong())).thenReturn(false);

            mockMvc.perform(
                            put("/roles/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPut)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }

        @Test
        public void updateByIdShouldReturn_400_OnInvalidJson() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            RoleDto toPut = assembler.toModel(new Role(1L, ""));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPut = objectMapper.writeValueAsString(toPut);

            when(roleService.existsById(anyLong())).thenReturn(false);

            mockMvc.perform(
                            put("/roles/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPut)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("BAD_REQUEST")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("Invalid JSON")))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].object", is("roleDto")))
                    .andExpect(jsonPath("$.errors[0].field", is("name")))
                    .andExpect(jsonPath("$.errors[0].rejectedValue", is("")))
                    .andExpect(jsonPath("$.errors[0].message", is("must not be blank")));
        }

        @Test
        public void updateByIdShouldReturn_200_OnValidUpdate() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            when(roleService.findById(anyLong())).thenReturn(Optional.empty());

            mockMvc.perform(get("/roles/1").accept(MediaTypes.HAL_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }

    @Nested
    class DeleteByIdTests {

        @Test
        public void deleteShouldReturn_201_IfRoleExists() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);


            when(roleService.existsById(anyLong())).thenReturn(true);
            doNothing().when(roleService).deleteById(anyLong());

            mockMvc.perform(delete("/roles/1"))
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andExpect(jsonPath("$.*").doesNotExist());
        }

        @Test
        public void deleteShouldReturn_404_IfRoleDoesNotExists() throws Exception {
            initSecurityContextWithUser(ReadOnlyUser);

            when(roleService.existsById(anyLong())).thenReturn(false);
            doNothing().when(roleService).deleteById(anyLong());

            mockMvc.perform(delete("/roles/1"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }

    @Nested
    class FindPrivilegesInRoleTests {

        @Test
        public void findPrivilegesInRoleShouldReturnPrivilegesInRole() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            List<PrivilegeDto> privilegeDtos = Stream.of(
                    new Privilege(1L, "P1"),
                    new Privilege(2L, "P2")
            ).map(privilegeAssembler::toModel).toList();

            when(roleService.existsById(anyLong())).thenReturn(true);
            when(privilegeService.findAllByRoleId(anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(new PageImpl<>(privilegeDtos));

            mockMvc.perform(get("/roles/1/privileges"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$._embedded.privileges.length()", is(2)));
        }

        @Test
        public void findPrivilegesInRoleShouldReturn_404_OnInvalidId() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            List<PrivilegeDto> privilegeDtos = Stream.of(
                    new Privilege(1L, "P1"),
                    new Privilege(2L, "P2")
            ).map(privilegeAssembler::toModel).toList();

            when(roleService.existsById(anyLong())).thenReturn(false);
            when(privilegeService.findAllByRoleId(anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(new PageImpl<>(privilegeDtos));

            mockMvc.perform(get("/roles/1999/privileges"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }

    @Nested
    class AddNewPrivilegeTests {

        @Test
        public void AddNewPrivilegeToRoleShouldReturnPrivilegesInRole() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            PrivilegeDto toPost = privilegeAssembler.toModel(new Privilege(1L, "P1"));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            when(roleService.existsById(anyLong())).thenReturn(true);
            when(roleService.addNewPrivilege(anyLong(), ArgumentMatchers.any(PrivilegeDto.class)))
                    .thenReturn(toPost);

            mockMvc.perform(
                            post("/roles/1/privileges")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.*", hasSize(2)))
                    .andExpect(jsonPath("$.name", is(toPost.getName())));
        }


        @Test
        public void AddNewPrivilegeToRoleShouldReturn_404_OnInvalidId() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            PrivilegeDto toPost = privilegeAssembler.toModel(new Privilege(1L, "P1"));
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonToPost = objectMapper.writeValueAsString(toPost);

            when(roleService.existsById(anyLong())).thenReturn(false);
            when(roleService.addNewPrivilege(anyLong(), ArgumentMatchers.any(PrivilegeDto.class)))
                    .thenReturn(toPost);

            mockMvc.perform(
                            post("/roles/1/privileges")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonToPost)
                                    .characterEncoding(Charset.defaultCharset())
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }

    @Nested
    class FindAllUsersWithRoleTests {
        @Test
        public void findPrivilegesInRoleShouldReturnPrivilegesInRole() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            List<OkrUserDto> okrUserDtos = Stream.of(
                    new OkrUser(1L, "name1", "surname1", "na.su1", "password1"),
                    new OkrUser(2L, "name2", "surname2", "na.su2", "password2")
            ).map(okrUserAssembler::toModel).toList();

            when(roleService.existsById(anyLong())).thenReturn(true);
            when(okrUserService.findAllUsersWithRole(anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(new PageImpl<>(okrUserDtos));

            mockMvc.perform(get("/roles/1/users"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.*", hasSize(3)))
                    .andExpect(jsonPath("$._embedded.users.length()", is(2)));
        }

        @Test
        public void findPrivilegesInRoleShouldReturn_404_OnInvalidId() throws Exception {
            initSecurityContextWithUser(AccessPrivilegesUser);

            List<OkrUserDto> okrUserDtos = Stream.of(
                    new OkrUser(1L, "name1", "surname1", "na.su1", "password1"),
                    new OkrUser(2L, "name2", "surname2", "na.su2", "password2")
            ).map(okrUserAssembler::toModel).toList();

            when(roleService.existsById(anyLong())).thenReturn(false);
            when(okrUserService.findAllUsersWithRole(anyLong(), ArgumentMatchers.any(Pageable.class)))
                    .thenReturn(new PageImpl<>(okrUserDtos));

            mockMvc.perform(get("/roles/1999/privileges"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.*", hasSize(4)))
                    .andExpect(jsonPath("$.httpStatus", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.message", is("No Role with this id exists")))
                    .andExpect(jsonPath("$.errors", nullValue()));
        }
    }
}
