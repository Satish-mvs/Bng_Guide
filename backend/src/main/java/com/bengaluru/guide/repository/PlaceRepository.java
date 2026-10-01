package com.bengaluru.guide.repository;

import com.bengaluru.guide.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategoryIgnoreCase(String category);

    List<Place> findByIsFamousTrue();

    @Query("SELECT p FROM Place p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.subCategory) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.area) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.tags) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.popularFor) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Place> searchPlaces(@Param("query") String query);

    @Query("SELECT p FROM Place p WHERE p.isFamous = true AND " +
           "(LOWER(p.category) = LOWER(:category) OR :category IS NULL)")
    List<Place> findFamousPlacesByCategory(@Param("category") String category);
}
