import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.Mapper;
import org.example.repository.UserRepository;
import org.example.service.KafkaEventProducer;
import org.example.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@EmbeddedKafka(partitions = 1, topics = "user-events")
public class SpringServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaEventProducer kafkaEventProducer;

    @Mock
    private Mapper<User, UserDTO> mapper;

    @Test
    void testGetUserById() {
        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setName("MockName");
        User entity = new User();
        entity.setId(1L);
        entity.setName("MockName");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(mapper.toDto(entity, UserDTO.class)).thenReturn(dto);

        UserDTO result = userService.getUserById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("MockName", result.getName());
    }

    @Test
    void testGetAllUSers() {
        List<UserDTO> dtos = new ArrayList<>();
        dtos.add(new UserDTO());
        dtos.add(new UserDTO());
        List<User> entities = new ArrayList<>();
        entities.add(new User());
        entities.add(new User());

        Mockito.when(userRepository.findAll()).thenReturn(entities);
        Mockito.when(mapper.toDtos(entities, UserDTO.class)).thenReturn(dtos);

        List<UserDTO> result = userService.getAllUsers();

        Assertions.assertEquals(2, result.size());
    }


    @Test
    void testCreateUser() {
        UserDTO dto = new UserDTO();
        dto.setName("MockName");
        User entity = new User();
        entity.setName("MockName");

        Mockito.when(mapper.toEntity(dto, User.class)).thenReturn(entity);
        Mockito.when(userRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity, UserDTO.class)).thenReturn(dto);

        UserDTO result = userService.createUser(dto);

        Assertions.assertEquals("MockName", result.getName());
    }

    @Test
    void testUpdateUser() {
        UserDTO dto = new UserDTO(1L, "MockName", null, null, null);
        User entity = new User(1L, "MockName", null, null, null);

        Mockito.when(mapper.toEntity(dto, User.class)).thenReturn(entity);
        Mockito.when(userRepository.save(entity)).thenReturn(entity);
        Mockito.when(mapper.toDto(entity, UserDTO.class)).thenReturn(dto);

        UserDTO result = userService.updateUser(dto);

        Assertions.assertEquals(dto.getId(), result.getId());
        Assertions.assertEquals(dto.getName(), result.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("MockName");
        user.setId(1L);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        Mockito.verify(userRepository).delete(user);
    }
}
