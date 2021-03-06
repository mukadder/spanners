package org.dontpanic.spanners.api.data;

import org.dontpanic.spanners.api.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import static org.dontpanic.spanners.api.stubs.SpannerBuilder.aTestSpanner;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Tests on the REST API exposed by this application.
 * Created by stevie on 10/06/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RestApiTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SpannerRepository repository;


    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    /**
     * By default, Spring Data REST does not expose database ids.
     * The ids are currently used by Spring MVC and must be exposed.
     */
    @Test
    public void testFindAllContainsDatabaseIds() throws Exception {

        // Setup: add a spanner to the repository
        Spanner spanner1 = aTestSpanner().named("Bertha").build();
        repository.save(spanner1);

        // Test: retrieve all spanners from repository
        mockMvc.perform(get("/spanners"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$._embedded.spanners[0].id", not(isEmptyString())));
    }
}
