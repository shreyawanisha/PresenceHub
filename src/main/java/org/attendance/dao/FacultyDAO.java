package org.attendance.dao;

import org.attendance.entity.Faculty;

import java.util.Optional;

public interface FacultyDAO extends GenericDAO<Faculty> {
    Optional<Faculty> findByUserId(long userId);
}