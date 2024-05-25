package ru.poldjoke.demo.jokebot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.poldjoke.demo.jokebot.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        userService.registerUser(username, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
