package com.tournament_organizer.dto.user;

import com.tournament_organizer.enums.Role;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserRoleDTO {
    @NonNull
    private Role role;
}
