import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.Mapper;
import org.example.mapper.impl.MapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SpringMapperTest {

    private final Mapper<User, UserDTO> mapper = new MapperImpl<>();

    @Test
    public void testToDto() {
        User entity = new User(1L, "John", "john@gmail.com", 22, null);

        UserDTO dto = mapper.toDto(entity, UserDTO.class);

        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void testToEntity() {
        UserDTO dto = new UserDTO(1L, "John", "john@gmail.com", 22, null);

        User entity = mapper.toEntity(dto, User.class);

        Assertions.assertEquals(entity.getId(), dto.getId());
        Assertions.assertEquals(entity.getName(), dto.getName());
    }

    @Test
    public void testToDtos() {
        User entity1 = new User(1L, "John", "john@gmail.com", 22, null);
        User entity2 = new User(2L, "Jane", "jane@gmail.com", 22, null);
        List<User> entities = List.of(entity1, entity2);

        List<UserDTO> dtos = mapper.toDtos(entities, UserDTO.class);

        Assertions.assertEquals(entities.get(0).getId(), dtos.get(0).getId());
        Assertions.assertEquals(entities.get(1).getName(), dtos.get(1).getName());
    }

    @Test
    public void testToEntities() {
        UserDTO dto1 = new UserDTO(1L, "John", "john@gmail.com", 22, null);
        UserDTO dto2 = new UserDTO(1L, "John", "john@gmail.com", 22, null);
        List<UserDTO> dtos = List.of(dto1, dto2);

        List<User> entities = mapper.toEntities(dtos, User.class);

        Assertions.assertEquals(entities.get(0).getId(), dtos.get(0).getId());
        Assertions.assertEquals(entities.get(1).getName(), dtos.get(1).getName());
    }
}
