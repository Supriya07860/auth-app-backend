package com.substring.auth.auth_app_backend.entities;

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

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID ID;
    @Column(name = "user_email", unique = true, length = 300)//UUID is used to generate unique identifiers
    private String email;
    @Column(name = "user_name", length = 500)
    private String name;
    private String password;
    private String image;
    private boolean enable = true;
    private Instant createdAt = Instant.now();              //Instant stores date and time in UTC format
    private Instant updatedAt = Instant.now();
//    private String gender;
//    private Address address;
    @Enumerated(EnumType.STRING)
    private Provider provider  = Provider.LOCAL;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))   //Default provider is LOCAL
    private Set<Role> roles = new HashSet<>();
    @PrePersist             //Method to set createdAt before persisting
    protected void onCreate() {
        Instant now = Instant.now();
        if (createdAt==null)
            createdAt = now;
            updatedAt = now;
    }
    @PreUpdate                  //Method to set updatedAt before updating
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
