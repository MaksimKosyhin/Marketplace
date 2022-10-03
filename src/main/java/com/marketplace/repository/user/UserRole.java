package com.marketplace.repository.user;

public enum UserRole {
    ADMIN(new UserAuthority[] {UserAuthority.READ_ANALYTICS}),
    MODERATOR(new UserAuthority[] {UserAuthority.WRITE_CONTENTS,
            UserAuthority.UPDATE_CONTENTS}),
    USER();

    private final UserAuthority[] authorities;
    UserRole(UserAuthority[] authorities) {
        this.authorities = authorities;
    }
    UserRole() {
        authorities = new UserAuthority[] {};
    }
    public UserAuthority[] getAuthorities() {
        return authorities;
    }
}
