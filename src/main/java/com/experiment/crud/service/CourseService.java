package com.experiment.crud.service;

import com.experiment.crud.entity.Course;
import com.experiment.crud.exception.CourseNotFoundException;
import com.experiment.crud.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * CourseService – Service layer for CRUD operations on Course.
 * Contains all business logic; controller only handles HTTP concerns.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // ─── Task 2: CRUD Operations ───────────────────────────────────────────

    /**
     * Create a new course.
     * Throws IllegalArgumentException if a course with the same title exists.
     */
    public Course createCourse(Course course) {
        Optional<Course> existing = courseRepository.findByTitleIgnoreCase(course.getTitle());
        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "A course with title '" + course.getTitle() + "' already exists.");
        }
        return courseRepository.save(course);
    }

    /**
     * Retrieve all courses.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Retrieve a single course by ID.
     * Throws CourseNotFoundException if not found.
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    /**
     * Update an existing course.
     * Partial-safe: only non-null fields from incoming object overwrite stored values.
     */
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));

        if (updatedCourse.getTitle() != null)
            existing.setTitle(updatedCourse.getTitle());
        if (updatedCourse.getInstructor() != null)
            existing.setInstructor(updatedCourse.getInstructor());
        if (updatedCourse.getCategory() != null)
            existing.setCategory(updatedCourse.getCategory());
        if (updatedCourse.getDescription() != null)
            existing.setDescription(updatedCourse.getDescription());
        if (updatedCourse.getDurationHours() != null)
            existing.setDurationHours(updatedCourse.getDurationHours());
        if (updatedCourse.getPrice() != null)
            existing.setPrice(updatedCourse.getPrice());
        if (updatedCourse.getLevel() != null)
            existing.setLevel(updatedCourse.getLevel());
        if (updatedCourse.getActive() != null)
            existing.setActive(updatedCourse.getActive());

        return courseRepository.save(existing);
    }

    /**
     * Delete a course by ID.
     * Throws CourseNotFoundException if not found.
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException(id);
        }
        courseRepository.deleteById(id);
    }

    // ─── Task 5: Search by Title ───────────────────────────────────────────

    /**
     * Search courses by partial title match (case-insensitive).
     */
    public List<Course> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Search title must not be blank.");
        }
        return courseRepository.searchByTitle(title.trim());
    }

    // ─── Filter helpers ───────────────────────────────────────────────────

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategoryIgnoreCase(category);
    }

    public List<Course> getCoursesByInstructor(String instructor) {
        return courseRepository.findByInstructorIgnoreCase(instructor);
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByActive(true);
    }

    public List<Course> getCoursesByLevel(String level) {
        return courseRepository.findByLevelIgnoreCase(level);
    }

    public List<Course> getCoursesByPriceRange(Double min, Double max) {
        return courseRepository.findByPriceBetweenOrderByPriceAsc(min, max);
    }

    public long getTotalCount() {
        return courseRepository.count();
    }
}
