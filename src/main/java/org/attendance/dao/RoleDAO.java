package org.attendance.dao;

import org.attendance.entity.Role;
import org.attendance.enums.RoleType;

public interface RoleDAO extends GenericDAO<Role> {
    public Role findByName(RoleType name);
}
