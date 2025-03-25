package org.attendance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.attendance.enums.RoleType;

@Setter
@Getter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    public Role() {
    }

    public Role(RoleType name) {
        this.name = name;
    }

}
