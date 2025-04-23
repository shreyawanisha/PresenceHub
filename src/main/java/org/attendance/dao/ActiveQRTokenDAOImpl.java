package org.attendance.dao;

import org.attendance.entity.ActiveQRToken;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class ActiveQRTokenDAOImpl extends GenericDAOImpl<ActiveQRToken> implements ActiveQRTokenDAO {

    public ActiveQRTokenDAOImpl() {
        super(ActiveQRToken.class);
    }

    @Override
    public Optional<ActiveQRToken> findByCourseIdAndDate(Long courseId, LocalDate date) {
        return getSession().createQuery(
                        "FROM ActiveQRToken a WHERE a.course.id = :courseId AND a.date = :date AND a.active = true",
                        ActiveQRToken.class)
                .setParameter("courseId", courseId)
                .setParameter("date", date)
                .getResultStream().findFirst();
    }

    @Override
    public void deactivateToken(Long courseId, LocalDate date) {
         getSession().createQuery("UPDATE ActiveQRToken a SET a.active = false WHERE a.course.id = :courseId AND a.date = :date")
                .setParameter("courseId", courseId)
                .setParameter("date", date)
                .executeUpdate();
    }
}
