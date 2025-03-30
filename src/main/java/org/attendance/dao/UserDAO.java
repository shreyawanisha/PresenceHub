package org.attendance.dao;

import org.attendance.entity.User;

public interface UserDAO extends GenericDAO<User> {

    User findByEmail(String email);
}
