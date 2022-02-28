package de.thbingen.epro;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.util.CamelCaseDisplayNameGenerator;
import de.thbingen.epro.util.UserLogin;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.server.core.AnnotationLinkRelationProvider;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DisplayNameGeneration(CamelCaseDisplayNameGenerator.class)
public abstract class IntegrationBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AnnotationLinkRelationProvider annotationLinkRelationProvider;

    public String doLogin(UserLogin userLogin) throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userLogin.getLoginJson())
                        .characterEncoding(Charset.defaultCharset())
        ).andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
}
