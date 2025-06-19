import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.SpringMain;
import org.example.controller.UserController;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = SpringMain.class)
@Import(SpringAPITest.MockConfig.class)
public class SpringAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        UserDTO mockUser = new UserDTO();
        mockUser.setId(1L);
        mockUser.setName("John");

        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testGetAll() throws Exception {
        List<UserDTO> mockUsers = new ArrayList<>();
        mockUsers.add(new UserDTO());
        mockUsers.add(new UserDTO());

        Mockito.when(userService.getAllUsers()).thenReturn(mockUsers);

        mockMvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testCreateUser() throws Exception {
        UserDTO user = new UserDTO(null, "John", "john@gmail.com", 22, null);
        UserDTO savedUser = new UserDTO(1L, "John", "john@gmail.com", 22, null);

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserDTO updateMockUser = new UserDTO(1L, "Judy", "judy@gmail.com", 22, null);

        Mockito.when(userService.updateUser(Mockito.any(UserDTO.class))).thenReturn(updateMockUser);

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Judy"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isOk());
    }
}
