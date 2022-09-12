package com.marketplace.user;

public enum UserRole {
    ADMIN(new UserAuthority[] {UserAuthority.READ_ANALYTICS}),
    MODERATOR(new UserAuthority[] {UserAuthority.WRITE_CONTENTS,
            UserAuthority.UPDATE_CONTENTS});

    private final UserAuthority[] authorities;
    UserRole(UserAuthority[] authorities) {
        this.authorities = authorities;
    }

    public UserAuthority[] getAuthorities() {
        return authorities;
    }
}
