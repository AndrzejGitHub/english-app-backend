package com.example.englishapp.integrations;

import com.example.englishapp.models.User;
import com.example.englishapp.models.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Order(1)
    @WithMockUser(roles = "ADMIN")
    @Test
    void saveUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestData.POST_ADD_USER)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    User savedUser = objectMapper.readValue(responseBody, User.class);

                    Assertions.assertEquals(1, savedUser.getId());
                    Assertions.assertEquals("Basia", savedUser.getFirstName());
                    Assertions.assertEquals("Nowak", savedUser.getLastName());
                    Assertions.assertEquals("basia@wp.pl", savedUser.getEmail());
                    Assertions.assertTrue(savedUser.getRoles().contains(UserRole.ROLE_USER));
                });
//                .andExpect(MockMvcResultMatchers.content().json(POST_ADDED_JSON));
    }

    @Order(2)
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(TestData.ADDED_USER_ARRAY));
    }

    @Order(3)
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(TestData.ADDED_USER_SINGLE));
    }

    @Order(4)
    @WithMockUser(roles = "ADMIN")
    @Test
    void editUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestData.PUT_EDIT_USER))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(TestData.PUTTED_USER));
    }

    @Order(5)
    @WithMockUser(roles = "ADMIN")
    @Test
    void getEditedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(TestData.PUTTED_USER));
    }
    @Order(6)
    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Order(7)
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUsersAfterDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Order(8)
    @WithMockUser(roles = "ADMIN")
    @Test
    void getUserAfterDelete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
