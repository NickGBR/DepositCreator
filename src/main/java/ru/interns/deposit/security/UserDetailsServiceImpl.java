package ru.interns.deposit.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.db.repositoiry.UsersRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository repository;

    @Autowired
    public UserDetailsServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
            User user = repository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        return SecurityUser.fromUser(user);
    }
}
