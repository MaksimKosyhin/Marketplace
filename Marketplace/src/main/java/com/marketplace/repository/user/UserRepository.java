package com.marketplace.repository.user;

public interface UserRepository {
    public long addUser(User user);
    public User getUser(String username);
}
