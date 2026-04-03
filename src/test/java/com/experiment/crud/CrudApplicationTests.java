package com.experiment.crud;

import com.experiment.crud.entity.Course;
import com.experiment.crud.repository.CourseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudApplicationTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CourseRepository courseRepository;

    // ── POST ──────────────────────────────────────────────────────────────

    @Test @Order(1)
    void createCourse_valid_returns201() throws Exception {
        Course c = new Course("JUnit Testing", "Tester Joe", "QA",
                "Unit and integration testing", 10, 19.99, "Beginner");
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("JUnit Testing"));
    }

    @Test @Order(2)
    void createCourse_missingTitle_returns400() throws Exception {
        Course c = new Course();
        c.setInstructor("X"); c.setCategory("Y"); c.setDurationHours(5); c.setPrice(9.99); c.setLevel("Beginner");
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(c)))
                .andExpect(status().isBadRequest());
    }

    // ── GET All ───────────────────────────────────────────────────────────

    @Test @Order(3)
    void getAllCourses_returns200() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    // ── GET by ID ─────────────────────────────────────────────────────────

    @Test @Order(4)
    void getCourseById_valid_returns200() throws Exception {
        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test @Order(5)
    void getCourseById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/courses/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ── PUT ───────────────────────────────────────────────────────────────

    @Test @Order(6)
    void updateCourse_valid_returns200() throws Exception {
        Course patch = new Course();
        patch.setPrice(39.99);
        mockMvc.perform(put("/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.price").value(39.99));
    }

    @Test @Order(7)
    void updateCourse_notFound_returns404() throws Exception {
        Course patch = new Course(); patch.setPrice(5.0);
        mockMvc.perform(put("/courses/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    // ── Search ────────────────────────────────────────────────────────────

    @Test @Order(8)
    void searchByTitle_found_returns200() throws Exception {
        mockMvc.perform(get("/courses/search/spring"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))));
    }

    @Test @Order(9)
    void searchByTitle_notFound_returns404() throws Exception {
        mockMvc.perform(get("/courses/search/xyznotexist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ── Filter ────────────────────────────────────────────────────────────

    @Test @Order(10)
    void filterByCategory_returns200() throws Exception {
        mockMvc.perform(get("/courses/category/Backend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value("Backend"));
    }

    @Test @Order(11)
    void filterByPrice_returns200() throws Exception {
        mockMvc.perform(get("/courses/filter?minPrice=20&maxPrice=60"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    // ── DELETE ────────────────────────────────────────────────────────────

    @Test @Order(12)
    void deleteCourse_valid_returns200() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test @Order(13)
    void deleteCourse_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/courses/9999"))
                .andExpect(status().isNotFound());
    }
}
