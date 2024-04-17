package ru.mirea.Ablautova.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.mirea.Ablautova.dto.UserModel;
import ru.mirea.Ablautova.security.jwt.JwtService;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = getToken(request);
        log.info(jwt);

        if (Objects.isNull(jwt)) {
            filterChain.doFilter(request, response);
            log.info("Нет jwt");
            return;
        }

        UserModel userModel = jwtService.parseToken(jwt);

        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getAuthorities()
        );

        context.setAuthentication(authenticationToken);

        log.info("Пользователь аутентифицирован");

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("jwt")) {
                    log.info("get jwt from cookie");
                    return cookie.getValue();
                }
            }
        }

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            return null;
        }

        final String[] strs = header.split(" ");
        if (strs.length != 2) {
            return null;
        }

        return strs[1].trim();
    }
}
