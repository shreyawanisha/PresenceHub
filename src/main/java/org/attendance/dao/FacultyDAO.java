package org.attendance.dao;

import org.attendance.entity.Faculty;

public interface FacultyDAO extends GenericDAO<Faculty> {
    Faculty findByUserId(long userId);
}
