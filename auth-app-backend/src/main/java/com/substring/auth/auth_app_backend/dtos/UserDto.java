package com.substring.auth.auth_app_backend.dtos;

import com.substring.auth.auth_app_backend.entities.Provider;
import com.substring.auth.auth_app_backend.entities.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID ID;
    private String email;
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();              //Instant stores date and time in UTC format
    private Instant updatedAt = Instant.now();
    private Provider provider  = Provider.LOCAL;            //Default provider is LOCAL
    private Set<RoleDto> roles = new HashSet<>();
}
