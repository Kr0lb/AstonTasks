package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserDTO;
import org.example.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Получение пользователя по id")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUser(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);

        EntityModel<UserDTO> model = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Получение всех пользователей")
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAllUsers() {
        List<EntityModel<UserDTO>> users = userService.getAllUsers().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()
                ))
                .toList();

        CollectionModel<EntityModel<UserDTO>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()
        );

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Создание пользователя")
    @PostMapping("/create")
    public ResponseEntity<EntityModel<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO created = userService.createUser(userDTO);
        EntityModel<UserDTO> model = EntityModel.of(created,
                linkTo(methodOn(UserController.class).getUser(created.getId())).withSelfRel()
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Обновление данных пользователя")
    @PutMapping("/update")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateUser(userDTO);
        EntityModel<UserDTO> model = EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getUser(updated.getId())).withSelfRel()
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
