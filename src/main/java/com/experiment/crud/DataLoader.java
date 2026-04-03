package com.experiment.crud;

import com.experiment.crud.entity.Course;
import com.experiment.crud.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DataLoader – seeds 15 sample courses into the H2 database on startup.
 */
@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seedCourses(CourseRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Course("Spring Boot Mastery",         "Alice Kumar",    "Backend",    "Complete Spring Boot guide",               35, 49.99,  "Beginner"));
                repo.save(new Course("React for Beginners",         "Bob Chen",       "Frontend",   "Modern React with Hooks",                  20, 29.99,  "Beginner"));
                repo.save(new Course("Advanced Java Programming",   "Carol Smith",    "Backend",    "Multithreading, streams and design patterns",40, 79.99, "Advanced"));
                repo.save(new Course("Docker & Kubernetes",         "David Lee",      "DevOps",     "Containerisation and orchestration",        25, 59.99,  "Intermediate"));
                repo.save(new Course("Python Data Science",         "Eve Williams",   "Data",       "Pandas, NumPy, Matplotlib",                 30, 44.99,  "Beginner"));
                repo.save(new Course("Machine Learning A-Z",        "Eve Williams",   "Data",       "Supervised and unsupervised learning",      50, 89.99,  "Advanced"));
                repo.save(new Course("AWS Cloud Practitioner",      "Frank Patel",    "Cloud",      "AWS fundamentals and certification prep",   20, 39.99,  "Beginner"));
                repo.save(new Course("Microservices Architecture",  "Alice Kumar",    "Backend",    "Design and deploy microservices",           45, 69.99,  "Advanced"));
                repo.save(new Course("Vue.js Complete Guide",       "Bob Chen",       "Frontend",   "Vue 3 Composition API",                    22, 34.99,  "Intermediate"));
                repo.save(new Course("SQL & Database Design",       "Grace Tan",      "Database",   "Relational databases and query optimisation",15, 24.99, "Beginner"));
                repo.save(new Course("TypeScript Deep Dive",        "Henry Brown",    "Frontend",   "TypeScript for large-scale apps",           18, 29.99,  "Intermediate"));
                repo.save(new Course("CI/CD with GitHub Actions",   "David Lee",      "DevOps",     "Automate builds, tests and deployments",    12, 19.99,  "Intermediate"));
                repo.save(new Course("System Design Interview",     "Carol Smith",    "Architecture","Design scalable systems",                  20, 59.99,  "Advanced"));
                repo.save(new Course("REST API Design with Spring", "Alice Kumar",    "Backend",    "Best practices for RESTful APIs",           18, 44.99,  "Intermediate"));
                repo.save(new Course("GraphQL Fundamentals",        "Henry Brown",    "Backend",    "Build and consume GraphQL APIs",            15, 34.99,  "Beginner"));
                System.out.println("✅ " + repo.count() + " sample courses loaded.");
            }
        };
    }
}
