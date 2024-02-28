package com.example.englishapp.integrations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PartOfSpeechControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;


//    @WithMockUser(roles = "ADMIN")
@Test
public void getPartOfSpeech() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/part-of-speech"))
            .andExpect(MockMvcResultMatchers.status().isOk());
//            .andExpect(MockMvcResultMatchers.content().json(JSON));
}


}
