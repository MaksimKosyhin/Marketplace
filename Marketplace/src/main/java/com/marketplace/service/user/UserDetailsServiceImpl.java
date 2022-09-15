package com.marketplace.service.user;

import com.marketplace.config.exception.AddEntryException;
import com.marketplace.repository.user.DbUser;
import com.marketplace.repository.user.UserAuthority;
import com.marketplace.repository.user.UserRepository;
import com.marketplace.repository.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
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

    public void addUser(DbUser user) {
        if (repository.addUser(user) == -1) {
            throw new AddEntryException("new user was not added");
        }
    }
}
