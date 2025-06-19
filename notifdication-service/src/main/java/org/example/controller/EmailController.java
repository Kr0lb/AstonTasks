package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public void sendEmail(@RequestParam String email, @RequestParam String subject, @RequestParam String body) {
        emailService.sendEmail(email, subject, body);
    }
}
