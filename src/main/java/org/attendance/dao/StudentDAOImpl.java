package org.attendance.dao;

import org.attendance.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class StudentDAOImpl extends GenericDAOImpl<Student> implements StudentDAO {
    public StudentDAOImpl() {
        super(Student.class);
    }

    @Override
    public Optional<Student> findByUserId(long userId) {
        return getSession().createQuery("SELECT s FROM Student s WHERE s.user.id = :userId", Student.class)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Student> findByRollNumber(String rollNumber) {
        return getSession().createQuery("SELECT s FROM Student s WHERE s.rollNumber = :rollNumber", Student.class)
                .setParameter("rollNumber", rollNumber)
                .uniqueResultOptional();
    }

    @Override
    public Optional<Student> findByUserEmail(String email) {
        return getSession().createQuery("FROM Student s WHERE s.user.email = :email", Student.class)
                .setParameter("email", email)
                .uniqueResultOptional();
    }
}
