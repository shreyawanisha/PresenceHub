package org.attendance.dao;

import org.attendance.entity.ActiveQRToken;

import java.time.LocalDate;
import java.util.Optional;

public interface ActiveQRTokenDAO extends GenericDAO<ActiveQRToken> {
    void save(ActiveQRToken token);
    Optional<ActiveQRToken> findByCourseIdAndDate(Long courseId, LocalDate date);
    void deactivateToken(Long courseId, LocalDate date);
}