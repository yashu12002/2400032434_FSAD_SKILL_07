package com.experiment.crud.controller;

import com.experiment.crud.dto.ApiResponse;
import com.experiment.crud.entity.Course;
import com.experiment.crud.exception.CourseNotFoundException;
import com.experiment.crud.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CourseController – REST API for Course CRUD operations.
 *
 * All methods return ResponseEntity<T> with appropriate HTTP status codes.
 *
 * Endpoints:
 *  POST   /courses                          – Create course           (201 Created)
 *  GET    /courses                          – Get all courses         (200 OK)
 *  GET    /courses/{id}                     – Get course by ID        (200 / 404)
 *  PUT    /courses/{id}                     – Update course           (200 / 404)
 *  DELETE /courses/{id}                     – Delete course           (200 / 404)
 *  GET    /courses/search/{title}           – Search by title         (200 / 404)
 *  GET    /courses/category/{category}      – Filter by category      (200)
 *  GET    /courses/instructor/{instructor}  – Filter by instructor    (200)
 *  GET    /courses/level/{level}            – Filter by level         (200)
 *  GET    /courses/active                   – Active courses only     (200)
 *  GET    /courses/filter?minPrice&maxPrice – Price range filter      (200)
 *  GET    /courses/count                    – Total count             (200)
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // ─── Task 3: POST – Create ─────────────────────────────────────────────

    /**
     * POST /courses
     * Creates a new course.
     * Returns 201 Created with the saved course.
     * Returns 409 Conflict if title already exists.
     *
     * Request body:
     * {
     *   "title":         "Spring Boot Mastery",
     *   "instructor":    "John Doe",
     *   "category":      "Backend",
     *   "description":   "Learn Spring Boot from scratch",
     *   "durationHours": 30,
     *   "price":         49.99,
     *   "level":         "Beginner"
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Course>> createCourse(
            @Valid @RequestBody Course course) {
        try {
            Course saved = courseService.createCourse(course);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.created("Course created successfully.", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ─── Task 3: GET All ──────────────────────────────────────────────────

    /**
     * GET /courses
     * Returns all courses.
     * Returns 200 OK with list (empty list if none exist).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Course>>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        String message = courses.isEmpty()
                ? "No courses found."
                : courses.size() + " course(s) retrieved.";
        return ResponseEntity.ok(ApiResponse.ok(message, courses));
    }

    // ─── Task 3: GET by ID ────────────────────────────────────────────────

    /**
     * GET /courses/{id}
     * Returns a single course by ID.
     * Returns 200 OK if found, 404 Not Found otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> getCourseById(
            @PathVariable Long id) {
        try {
            Course course = courseService.getCourseById(id);
            return ResponseEntity.ok(ApiResponse.ok("Course retrieved.", course));
        } catch (CourseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ─── Task 3: PUT – Update ─────────────────────────────────────────────

    /**
     * PUT /courses/{id}
     * Updates an existing course (partial update – only provided fields change).
     * Returns 200 OK if updated, 404 Not Found otherwise.
     *
     * Request body (any subset of fields):
     * {
     *   "price": 39.99,
     *   "level": "Intermediate"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Course>> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        try {
            Course updated = courseService.updateCourse(id, course);
            return ResponseEntity.ok(ApiResponse.ok("Course updated successfully.", updated));
        } catch (CourseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ─── Task 3: DELETE ───────────────────────────────────────────────────

    /**
     * DELETE /courses/{id}
     * Deletes a course by ID.
     * Returns 200 OK if deleted, 404 Not Found otherwise.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Course with ID " + id + " deleted successfully.", null));
        } catch (CourseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ─── Task 5: Search by Title ──────────────────────────────────────────

    /**
     * GET /courses/search/{title}
     * Searches courses by partial title match (case-insensitive).
     * Returns 200 OK with matching list.
     * Returns 404 Not Found if no matches.
     *
     * Example: GET /courses/search/spring
     */
    @GetMapping("/search/{title}")
    public ResponseEntity<ApiResponse<List<Course>>> searchByTitle(
            @PathVariable String title) {
        try {
            List<Course> results = courseService.searchByTitle(title);
            if (results.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false,
                                "No courses found matching title: '" + title + "'", null));
            }
            return ResponseEntity.ok(ApiResponse.ok(
                    results.size() + " course(s) found matching '" + title + "'.", results));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ─── Extra Endpoints ──────────────────────────────────────────────────

    /**
     * GET /courses/category/{category}
     * Returns courses in the given category.
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Course>>> getByCategory(
            @PathVariable String category) {
        List<Course> courses = courseService.getCoursesByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok(
                courses.size() + " course(s) in category '" + category + "'.", courses));
    }

    /**
     * GET /courses/instructor/{instructor}
     * Returns courses by instructor.
     */
    @GetMapping("/instructor/{instructor}")
    public ResponseEntity<ApiResponse<List<Course>>> getByInstructor(
            @PathVariable String instructor) {
        List<Course> courses = courseService.getCoursesByInstructor(instructor);
        return ResponseEntity.ok(ApiResponse.ok(
                courses.size() + " course(s) by instructor '" + instructor + "'.", courses));
    }

    /**
     * GET /courses/level/{level}
     * Returns courses by difficulty level (Beginner / Intermediate / Advanced).
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<ApiResponse<List<Course>>> getByLevel(
            @PathVariable String level) {
        List<Course> courses = courseService.getCoursesByLevel(level);
        return ResponseEntity.ok(ApiResponse.ok(
                courses.size() + " course(s) at level '" + level + "'.", courses));
    }

    /**
     * GET /courses/active
     * Returns only active courses.
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Course>>> getActiveCourses() {
        List<Course> courses = courseService.getActiveCourses();
        return ResponseEntity.ok(ApiResponse.ok(
                courses.size() + " active course(s) found.", courses));
    }

    /**
     * GET /courses/filter?minPrice=10&maxPrice=100
     * Returns courses within a price range.
     */
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<List<Course>>> filterByPrice(
            @RequestParam(defaultValue = "0") Double minPrice,
            @RequestParam(defaultValue = "9999") Double maxPrice) {
        List<Course> courses = courseService.getCoursesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.ok(
                courses.size() + " course(s) priced between $" + minPrice + " and $" + maxPrice + ".",
                courses));
    }

    /**
     * GET /courses/count
     * Returns total number of courses.
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getCourseCount() {
        long count = courseService.getTotalCount();
        return ResponseEntity.ok(ApiResponse.ok(
                "Total course count.", Map.of("total", count)));
    }
}
