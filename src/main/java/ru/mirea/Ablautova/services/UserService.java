package ru.mirea.Ablautova.services;

import ru.mirea.Ablautova.dto.request.LoginRequest;
import ru.mirea.Ablautova.dto.response.LoginResponse;
import ru.mirea.Ablautova.dto.request.RegisterRequest;
import ru.mirea.Ablautova.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
