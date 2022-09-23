package com.marketplace.service.user;

import com.marketplace.repository.user.DbUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public void addUser(DbUser user);
}
