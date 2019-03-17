package com.management.appuser;

import com.management.appuser.model.User;
import com.management.appuser.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerIntegrationTest extends TestUtil {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        userRepository.deleteAll();
    }

    @Test
    public void retirevingUsersListShouldReturn404_when_dataBaseIsEmpty() throws Exception {
        MvcResult result = mockMvc.perform(get("/app-user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().equals("[]"), equalTo(false));
    }

    @Test
    public void shouldReturnUsersList_when_dataBaseIsNotEmpty() throws Exception {
        userRepository.save(aUser(1, "name1", "1980-01-01"));
        userRepository.save(aUser(2, "name2", "1980-01-02"));

        MvcResult result = mockMvc.perform(get("/app-user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(aListJSonResponse(), result.getResponse().getContentAsString());
    }

    @Test
    public void shouldReturn404_when_userIdNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/app-user/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().equals("[]"), equalTo(false));
    }

    @Test
    public void shouldReturnUser_when_userIdExists() throws Exception {
        userRepository.save(aUser(1, "name1", "1980-01-01"));

        MvcResult result = mockMvc.perform(get("/app-user/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(aJSonResponse(), result.getResponse().getContentAsString());
    }

    @Test
    public void shouldCreateNewUser_when_validDataProvided() throws Exception {
        String aPostUser = "{\"id\":\"1\",\"name\":\"name1\",\"birthDate\":\"1980-01-01\"}";
        mockMvc.perform(post("/app-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aPostUser))
                .andExpect(status().isCreated());

        User user = userRepository.findById(1).get();
        assertEquals(1, user.getId());
        assertEquals("name1", user.getName());
        assertEquals("1980-01-01", user.getBirthDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void shouldReturn500_when_creatingUserWithNotValidData() throws Exception {
        String aPostUser = "{\"id\":null,\"name\":\"null\",\"birthDate\":\"null\"}";
        MvcResult result = mockMvc.perform(post("/app-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aPostUser))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().equals("[]"), equalTo(false));
    }

    @Test
    public void shouldReturn404_when_modifyingNonExistingUser() throws Exception {
        String aPatchUser = "{\"name\":\"any\",\"birthDate\":\"any\"}";
        MvcResult result = mockMvc.perform(patch("/app-user/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aPatchUser))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().equals("[]"), equalTo(false));
    }

    @Test
    public void shouldModifyUser_when_validDataProvided() throws Exception {
        userRepository.save(aUser(1, "name", "1980-01-01"));
        String aPatchUser = "{\"name\":\"newName\",\"birthDate\":\"1950-02-03\"}";
        MvcResult result = mockMvc.perform(patch("/app-user/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aPatchUser))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":\"1\",\"name\":\"newName\",\"birthDate\":\"1950-02-03\",\"createdAt\":\"1900-01-01 01:01:00\"}", result.getResponse().getContentAsString());
    }

    @Test
    public void shouldReturn404_when_deletingNonExistingUser() throws Exception {
        MvcResult result = mockMvc.perform(delete("/app-user/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        assertThat(result.getResponse().getContentAsString().equals("[]"), equalTo(false));
    }

    @Test
    public void shouldDeleteUser_when_validIdProvided() throws Exception {
        userRepository.save(aUser(1, "name", "1980-01-01"));
        mockMvc.perform(delete("/app-user/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertFalse(userRepository.findById(1).isPresent());
    }
}
