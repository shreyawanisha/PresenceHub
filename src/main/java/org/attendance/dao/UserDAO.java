package org.attendance.dao;

import org.attendance.entity.User;

import java.util.Optional;

public interface UserDAO extends GenericDAO<User> {
    Optional<User> findByEmail(String email);
}
