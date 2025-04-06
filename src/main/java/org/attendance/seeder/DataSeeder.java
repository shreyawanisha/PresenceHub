package org.attendance.seeder;

import org.attendance.dao.RoleDAO;
import org.attendance.entity.Role;
import org.attendance.enums.RoleType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleDAO roleDAO;

    public DataSeeder(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional
    public void run(String... args) {
        for (RoleType roleType : RoleType.values()) {
            if (roleDAO.findByName(roleType).isEmpty()) {
                Role role = new Role();
                role.setName(roleType);
                roleDAO.save(role);
                System.out.println("✅ Inserted role: " + roleType);
            }
        }
    }
}