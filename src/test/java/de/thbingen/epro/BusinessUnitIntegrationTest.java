package de.thbingen.epro;


import de.thbingen.epro.controller.LoginController;
import de.thbingen.epro.repository.BusinessUnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {"de.thbingen.epro"})
@AutoConfigureMockMvc
class BusinessUnitIntegrationTest {

    Logger logger = LoggerFactory.getLogger(BusinessUnitIntegrationTest.class);


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LoginController loginController;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @LocalServerPort
    private Integer port;

    public String doLogin() throws Exception {
        return loginController.login(Map.of("username", "vor.nach1", "password", "password1"));
    }

    @Test
    @Transactional
    void dbIsInitializedCorrectly() throws Exception {
        String token = doLogin();

        /*WebTestClient testClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/api/v1")
                .build();

        testClient.get()
                .uri("/businessUnits")
                .header("Authorization", "Bearer" + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.*");*/
        mockMvc.perform(
                get("/businessUnits")
                        .header("Authorization", "Bearer" + token)
        ).andDo(print()).andExpect(status().isOk());
    }
}
