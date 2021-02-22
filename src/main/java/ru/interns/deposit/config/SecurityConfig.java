    package ru.interns.deposit.config;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Qualifier;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
    import org.springframework.security.web.util.matcher.OrRequestMatcher;
    import org.springframework.security.web.util.matcher.RequestMatcher;
    import org.springframework.util.AntPathMatcher;
    import ru.interns.deposit.helper.Api;

    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        private final PasswordEncoder passwordEncoder;
        private final UserDetailsService detailsService;

        @Autowired
        public SecurityConfig(PasswordEncoder passwordEncoder,
                              @Qualifier("userDetailsServiceImpl") UserDetailsService detailsService) {
            this.passwordEncoder = passwordEncoder;
            this.detailsService = detailsService;
        }

        // Url доступные анонимным пользователям
        private static final RequestMatcher PUBLIC_API_URLS = new OrRequestMatcher(
                new AntPathRequestMatcher(Api.REGISTRATION_POST_REQUEST.getUrl()),
                new AntPathRequestMatcher(Api.LOGIN_PAGE.getUrl()),
                new AntPathRequestMatcher(Api.REGISTRATION_PAGE.getUrl()),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/image/**"),
                new AntPathRequestMatcher("/favicon.ico")
                //new AntPathRequestMatcher("/api/**")

        );

        private static final RequestMatcher USER_API_URLS = new OrRequestMatcher(
                new AntPathRequestMatcher("/api/view/success")
        );


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .requestMatchers(PUBLIC_API_URLS)
                    .permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage(Api.LOGIN_PAGE.getUrl())
                    .permitAll()
                    .and()
                    .csrf().disable()
                    .logout().disable();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(daoAuthenticationProvider());
        }

        @Bean
        protected DaoAuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            authenticationProvider.setUserDetailsService(detailsService);
            return authenticationProvider;
        }
    }
