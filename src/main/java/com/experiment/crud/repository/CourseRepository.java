package com.experiment.crud.repository;

import com.experiment.crud.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Search by title – case-insensitive partial match (Task 5)
    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Course> searchByTitle(@Param("title") String title);

    // Filter by category
    List<Course> findByCategoryIgnoreCase(String category);

    // Filter by instructor
    List<Course> findByInstructorIgnoreCase(String instructor);

    // Filter active courses
    List<Course> findByActive(Boolean active);

    // Filter by level
    List<Course> findByLevelIgnoreCase(String level);

    // Price range filter
    List<Course> findByPriceBetweenOrderByPriceAsc(Double minPrice, Double maxPrice);

    // Check duplicate title
    Optional<Course> findByTitleIgnoreCase(String title);
}
