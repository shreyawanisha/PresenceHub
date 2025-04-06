package org.attendance.dao;

import org.attendance.entity.Role;
import org.attendance.enums.RoleType;

import java.util.Optional;

public interface RoleDAO extends GenericDAO<Role> {
    Optional<Role> findByName(RoleType name);}
