package org.example.dto;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.entity.User}
 */

@Value
public class UserDTO implements Serializable {
    Long id;
    String name;
    String email;
    Integer age;
    LocalDateTime createdAt;
}