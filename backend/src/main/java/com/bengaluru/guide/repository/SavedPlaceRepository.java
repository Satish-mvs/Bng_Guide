package com.bengaluru.guide.repository;

import com.bengaluru.guide.entity.SavedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedPlaceRepository extends JpaRepository<SavedPlace, Long> {
    List<SavedPlace> findAllByOrderBySavedAtDesc();
    Optional<SavedPlace> findByPlaceId(Long placeId);
    void deleteByPlaceId(Long placeId);
}
