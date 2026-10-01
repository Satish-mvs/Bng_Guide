package com.bengaluru.guide.repository;

import com.bengaluru.guide.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByKeyIgnoreCase(String key);
    List<Category> findAllByOrderByDisplayOrderAsc();
}
