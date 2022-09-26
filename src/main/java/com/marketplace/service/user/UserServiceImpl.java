package com.marketplace.service.user;

import com.marketplace.config.exception.AddEntryException;
import com.marketplace.repository.user.DbUser;
import com.marketplace.repository.user.UserAuthority;
import com.marketplace.repository.user.UserRepository;
import com.marketplace.repository.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    private Set<GrantedAuthority> getGrantedAuthorities(UserRole role) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (UserAuthority authority : role.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority.name()));
        }
        authorities.add(new SimpleGrantedAuthority(String.format("ROLE_$s", role.name())));

        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DbUser user = repository.getUser(username);

        return new User(
                user.getUsername(),
                user.getPassword(),
                getGrantedAuthorities(user.getRole())
        );
    }

    @Override
    public void addUser(DbUser user) {

        if (repository.userExists(user.getUsername())) {
            throw new AddEntryException(
                    String.format("user with username: %s already exists", user.getUsername()));
        } else {
            user.setPassword(encoder.encode(user.getPassword()));

            if (repository.addUser(user) == -1) {
                throw new AddEntryException("new user was not added");
            }
        }
    }
}
