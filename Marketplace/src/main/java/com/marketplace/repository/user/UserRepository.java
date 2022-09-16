package com.marketplace.repository.user;

public interface UserRepository {
    public boolean userExists(String username);

    public long addUser(DbUser dbUser);
    public DbUser getUser(String username);
}
