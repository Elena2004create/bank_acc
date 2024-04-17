package ru.mirea.Ablautova.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mirea.Ablautova.dto.UserModel;
import ru.mirea.Ablautova.dto.request.LoginRequest;
import ru.mirea.Ablautova.dto.response.LoginResponse;
import ru.mirea.Ablautova.dto.request.RegisterRequest;
import ru.mirea.Ablautova.dto.response.RegisterResponse;
import ru.mirea.Ablautova.entities.UserEntity;
import ru.mirea.Ablautova.repositories.UsersRepository;
import ru.mirea.Ablautova.security.jwt.JwtService;

import javax.security.auth.login.CredentialException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if(usersRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Пользователь уже зарегистрирован");
        }

        UserEntity user = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        user = usersRepository.save(user);

       UserModel userModel = UserModel.builder()
                                        .email(user.getEmail())
                                        .build();

       String jwt = jwtService.generateToken(userModel);

       return new RegisterResponse(user.getEmail(), jwt);
    }

    @Override
    @SneakyThrows
    public LoginResponse login(LoginRequest loginRequest) {
        if(usersRepository.findByEmail(loginRequest.getEmail()).isEmpty()){
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        UserEntity user = usersRepository.findByEmail(loginRequest.getEmail()).get();

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new CredentialException("Неправильный логин или пароль");
        }

        UserModel userModel = UserModel.builder()
                .email(user.getEmail())
                .build();

        String jwt = jwtService.generateToken(userModel);

        return new LoginResponse(userModel.getEmail(), jwt);
    }
}
