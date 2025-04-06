package org.attendance.dao;

import org.attendance.entity.Role;
import org.attendance.enums.RoleType;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDAO {
    public RoleDAOImpl() {
        super(Role.class);
    }

    @Override
    public Role findByName(RoleType name) {
        try {
            return getSession().createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
