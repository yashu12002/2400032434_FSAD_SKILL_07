package com.experiment.crud.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Course Entity – mapped to the "courses" table.
 */
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Instructor name is required")
    @Column(nullable = false)
    private String instructor;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @Column(length = 1000)
    private String description;

    @NotNull(message = "Duration (hours) is required")
    @Min(value = 1, message = "Duration must be at least 1 hour")
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private String level;          // Beginner / Intermediate / Advanced

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ─── Constructors ──────────────────────────────────────────────────────

    public Course() {}

    public Course(String title, String instructor, String category,
                  String description, Integer durationHours,
                  Double price, String level) {
        this.title = title;
        this.instructor = instructor;
        this.category = category;
        this.description = description;
        this.durationHours = durationHours;
        this.price = price;
        this.level = level;
        this.active = true;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
