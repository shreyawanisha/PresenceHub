package org.attendance.dao;

import jakarta.persistence.TypedQuery;
import org.attendance.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends GenericDAOImpl<User> implements UserDAO {
    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) {
       try{
           final TypedQuery<User> query = getSession().createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
           query.setParameter("email", email);
              return query.getSingleResult();
       }catch (Exception e){
              return null;
       }
    }
}
