package ru.mirea.Ablautova.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.mirea.Ablautova.dto.UserModel;
import ru.mirea.Ablautova.entities.UserEntity;
import ru.mirea.Ablautova.repositories.UsersRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        return UserModel.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
