package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.entity.User}
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "DTO Пользователя")
public class UserDTO {
    @Schema(title = "Id")
    private Long id;
    @Schema(title = "Имя")
    private String name;
    @Schema(title = "Почта")
    private String email;
    @Schema(title = "Возраст")
    private Integer age;
    @Schema(title = "Дата создания")
    private LocalDateTime createdAt;
}