package org.attendance.dao;

import jakarta.persistence.NoResultException;
import org.attendance.entity.Role;
import org.attendance.enums.RoleType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDAO {
    public RoleDAOImpl() {
        super(Role.class);
    }

    @Override
    public Optional<Role> findByName(RoleType name) {
        try {
            Role role = getSession().createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}