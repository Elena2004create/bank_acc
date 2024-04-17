package ru.mirea.Ablautova.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.Ablautova.dto.request.LoginRequest;
import ru.mirea.Ablautova.dto.request.RegisterRequest;
import ru.mirea.Ablautova.dto.response.LoginResponse;
import ru.mirea.Ablautova.dto.response.RegisterResponse;
import ru.mirea.Ablautova.services.UserService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse servletResponse){
        LoginResponse loginResponse = userService.login(loginRequest);
        servletResponse.addCookie(new Cookie("jwt", loginResponse.getJwt()));
        return ResponseEntity.ok(loginResponse);
    }

}
