package com.experiment.crud.exception;

public class CourseNotFoundException extends RuntimeException {
    private final Long courseId;

    public CourseNotFoundException(Long courseId) {
        super("Course not found with ID: " + courseId);
        this.courseId = courseId;
    }

    public CourseNotFoundException(String message) {
        super(message);
        this.courseId = null;
    }

    public Long getCourseId() { return courseId; }
}
