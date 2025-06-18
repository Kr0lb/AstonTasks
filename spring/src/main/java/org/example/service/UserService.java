package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.Mapper;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KafkaEventProducer kafkaEventProducer;

    private final UserRepository userRepository;
    private final Mapper<User, UserDTO> mapper;

    public UserDTO createUser(UserDTO userDTO) {
        User user = mapper.toEntity(userDTO, User.class);
        user = userRepository.save(user);
        kafkaEventProducer.send("user-create", "пользователь создан");
        return mapper.toDto(user, UserDTO.class);
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = mapper.toEntity(userDTO, User.class);
        user = userRepository.save(user);
        return mapper.toDto(user, UserDTO.class);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        kafkaEventProducer.send("user-delete", "пользователь удален");
        userRepository.delete(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return mapper.toDtos(users, UserDTO.class);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return mapper.toDto(user, UserDTO.class);
    }
}
