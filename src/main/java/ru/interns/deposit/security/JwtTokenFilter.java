package ru.interns.deposit.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.interns.deposit.exceptions.JwtAuthenticationException;
import ru.interns.deposit.util.Api;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (!checkRequestURL(servletRequest) && token != null && jwtTokenProvider.validateToken(token)) {
                final Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        catch (JwtAuthenticationException e){
            SecurityContextHolder.clearContext();
            ((HttpServletResponse)servletResponse).sendError(e.getStatus().value());
            throw new JwtAuthenticationException("Jwt token is expired or invalid", e.getStatus());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * Нам нужна аутентификация только для защищенных страниц, но фильтр срабатывает для всех страниц.
     * @param request проверяемый запрос
     * @return
     */
    private boolean checkRequestURL(ServletRequest request){
        final String servletPath = ((HttpServletRequest) request).getServletPath();
        return servletPath.equals(Api.LOGIN_PAGE.getUrl()) ||
                servletPath.equals(Api.REGISTRATION_PAGE.getUrl());
    }
}
