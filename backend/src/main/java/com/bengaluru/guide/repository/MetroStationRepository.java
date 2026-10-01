package com.bengaluru.guide.repository;

import com.bengaluru.guide.entity.MetroStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MetroStationRepository extends JpaRepository<MetroStation, Long> {
    List<MetroStation> findByLineOrderBySequenceOrderAsc(String line);
    Optional<MetroStation> findByNameIgnoreCase(String name);
    List<MetroStation> findByIsInterchangeTrue();
}
