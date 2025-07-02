package org.example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping
    public ResponseEntity<String> userServiceFallback() {
        return ResponseEntity.ok("Сервис временно недоступен. Попробуйте позже");
    }
}
