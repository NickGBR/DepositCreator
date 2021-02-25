package ru.interns.deposit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.interns.deposit.security.JwtTokenFilter;
import ru.interns.deposit.util.Api;
//import ru.interns.deposit.security.JwtConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final JwtTokenFilter tokenFilter;

    @Autowired
    public SecurityConfig(
                          JwtTokenFilter tokenFilter) {

        this.tokenFilter = tokenFilter;
    }

    // Url доступные анонимным пользователям
    private static final RequestMatcher PUBLIC_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher(Api.REGISTRATION_POST_REQUEST.getUrl()),
            new AntPathRequestMatcher(Api.LOGIN_POST_REQUEST.getUrl()),

            new AntPathRequestMatcher(Api.LOGIN_PAGE.getUrl()),
            new AntPathRequestMatcher(Api.REGISTRATION_PAGE.getUrl()),
            new AntPathRequestMatcher("/css/**"),
            new AntPathRequestMatcher("/js/**"),
            new AntPathRequestMatcher("/image/**"),
            new AntPathRequestMatcher("/favicon.ico")


    );

    private static final RequestMatcher USER_API_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/view/success")
    );

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.httpBasic().disable()
                .csrf().disable()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(PUBLIC_API_URLS).permitAll()
                .anyRequest()
                .authenticated();
                //.and()
                //.apply(jwtConfigurer);
    }


//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .csrf().disable()
//            .authorizeRequests()
//            .requestMatchers(PUBLIC_API_URLS)
//            .permitAll();
    // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    // .and()
    //   .authorizeRequests()
//                    .requestMatchers(PUBLIC_API_URLS)
//                    .permitAll()
    //   .anyRequest()
    //     .permitAll();
    //.authenticated()
    // .and()
    //.apply(jwtConfigurer)
//                    .and()
//                    .formLogin()
//                    .loginPage(Api.LOGIN_PAGE.getUrl())
//                    .permitAll();
//                    .and()
//                    .logout().disable();
//        }

//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.authenticationProvider(daoAuthenticationProvider());
//        }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }


//        @Bean
//        protected DaoAuthenticationProvider daoAuthenticationProvider() {
//            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//            authenticationProvider.setPasswordEncoder(passwordEncoder);
//            authenticationProvider.setUserDetailsService(detailsService);
//            return authenticationProvider;
//        }
}
