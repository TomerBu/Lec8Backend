package edu.tomerbu.lec4tdd.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.GeneratedValue;
import javax.persistence.*;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class User implements UserDetails {
    @Id
    @GeneratedValue
    @JsonView(Views.Basic.class)
    private long id;

    @Size(min = 2, max = 255)
    @NotNull(message = "{edu.tomerbu.constraints.username.NotNull.message}")
    @UniqueUserName(message = "User Name must be unique")
    @JsonView(Views.Basic.class)
    private String userName;

    @NotNull
    @Size(min = 2, max = 255)
    @JsonView(Views.Basic.class)
    private String displayName;
    //save the password as plain text
    @NotNull
    @Size(min = 6, max = 255)
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@$])(?=.*\\d).*$")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "{edu.tomerbu.constraints.password.Pattern.message}"
    )
    @JsonView(Views.Sensitive.class)
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getUserName() {
        return userName;
    }

    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //ADMIN/USER/DEV/
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }
    @JsonIgnore
    @Transient
    @Override
    public String getUsername() {
        return userName;
    }

    @JsonIgnore
    @Transient//do not save in db
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }
}
