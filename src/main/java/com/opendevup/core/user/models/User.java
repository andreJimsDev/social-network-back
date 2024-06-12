package com.opendevup.core.user.models;

import com.opendevup.core.shared.model.Email;
import com.opendevup.core.shared.model.EntityAudit;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User extends EntityAudit {
    private UserId userId;
    private String firstName;
    private String lastName;
    private Email email;
    private String password;
    private Role role;

    public String fullName() {
        return firstName + " " + lastName;
    }
}