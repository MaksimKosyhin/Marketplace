package com.marketplace.user;

public interface UserRepository {
    public long addUser(User user);
    public User getUser(String username);
}
