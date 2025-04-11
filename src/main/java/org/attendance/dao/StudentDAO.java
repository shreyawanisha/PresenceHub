package org.attendance.dao;

import org.attendance.entity.Student;
import java.util.Optional;

public interface StudentDAO extends GenericDAO<Student>{
    Optional<Student> findByUserId(long userId);
    Optional<Student> findByRollNumber(String rollNumber);

    Optional<Student> findByUserEmail(String email);
}
