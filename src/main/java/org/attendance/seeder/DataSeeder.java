package org.attendance.seeder;

import org.attendance.dao.RoleDAO;
import org.attendance.entity.Role;
import org.attendance.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public void run(String... args) {
        String[] roles = {"ADMIN", "FACULTY", "STUDENT"};

        for (String name : roles) {
            final RoleType roleName = RoleType.valueOf(name);
            if (roleDAO.findByName(roleName) == null) {
                Role role = new Role();
                role.setName(roleName);
                roleDAO.save(role);
                System.out.println("✅ Inserted role: " + name);
            }
        }
    }
}
