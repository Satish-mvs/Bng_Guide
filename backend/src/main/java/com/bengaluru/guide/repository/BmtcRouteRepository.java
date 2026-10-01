package com.bengaluru.guide.repository;

import com.bengaluru.guide.entity.BmtcRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BmtcRouteRepository extends JpaRepository<BmtcRoute, Long> {

    @Query("SELECT r FROM BmtcRoute r WHERE " +
           "LOWER(r.origin) LIKE LOWER(CONCAT('%', :origin, '%')) OR " +
           "LOWER(r.destination) LIKE LOWER(CONCAT('%', :destination, '%')) OR " +
           "LOWER(r.viaStops) LIKE LOWER(CONCAT('%', :origin, '%')) OR " +
           "LOWER(r.viaStops) LIKE LOWER(CONCAT('%', :destination, '%'))")
    List<BmtcRoute> findConnectingRoutes(@Param("origin") String origin, @Param("destination") String destination);

    @Query("SELECT r FROM BmtcRoute r WHERE " +
           "LOWER(r.origin) LIKE LOWER(CONCAT('%', :stopName, '%')) OR " +
           "LOWER(r.destination) LIKE LOWER(CONCAT('%', :stopName, '%')) OR " +
           "LOWER(r.viaStops) LIKE LOWER(CONCAT('%', :stopName, '%'))")
    List<BmtcRoute> findRoutesPassingStop(@Param("stopName") String stopName);
}
