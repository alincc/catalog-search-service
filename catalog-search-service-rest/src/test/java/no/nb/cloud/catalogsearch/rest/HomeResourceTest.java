package no.nb.cloud.catalogsearch.rest;

import no.nb.microservices.catalogsearch.Application;
import no.nb.microservices.catalogsearch.rest.HomeResource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class HomeResourceTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        HomeResource homeResource = new HomeResource();
        mockMvc = MockMvcBuilders.standaloneSetup(homeResource).build();
    }

    @Test
    public void helloWorldTest() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

}
