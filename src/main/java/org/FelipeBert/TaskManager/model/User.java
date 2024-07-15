package org.FelipeBert.TaskManager.model;

import jakarta.persistence.*;
import lombok.*;
import org.FelipeBert.TaskManager.model.enums.UserRole;
import org.FelipeBert.TaskManager.model.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taskList = new ArrayList<>();

    public User(String name, String login, String email, String password, LocalDateTime creationDate,
                LocalDateTime updateDate, UserStatus userStatus, UserRole userRole, List<Task> taskList) {
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.userStatus = userStatus;
        this.userRole = userRole;
        this.taskList = taskList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userRole == UserRole.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }
        return List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addOnList(Task task){
        this.taskList.add(task);
    }
}
