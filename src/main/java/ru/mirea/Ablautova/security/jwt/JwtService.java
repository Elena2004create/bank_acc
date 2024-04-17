package ru.mirea.Ablautova.security.jwt;

import ru.mirea.Ablautova.dto.UserModel;

public interface JwtService {

    String generateToken(UserModel userModel);

    UserModel parseToken(String jwt);
}
