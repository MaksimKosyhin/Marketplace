package com.marketplace.repository.user;

public interface UserRepository {
    public long addUser(DbUser dbUser);

    public DbUser getUser(String username);
}
