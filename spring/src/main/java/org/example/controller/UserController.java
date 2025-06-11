package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
