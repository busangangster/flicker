package com.flicker.user.user.domain.vo;

import com.flicker.user.user.domain.UserGrade;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;


@Embeddable
@Getter
/*
    Role은 'ADMIN', 'USER' 둘 중 하나만 가능하다.
 */
public class Role {

    private final String role;

    public Role() {
        this(UserGrade.USER);
    }

    public Role(UserGrade grade) {
        this.role = grade.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(role, role1.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(role);
    }
}
