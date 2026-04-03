# Experiment 7 – REST API CRUD Operations using ResponseEntity

## Overview
A Spring Boot REST API managing **Course** records, demonstrating all four CRUD operations and a search endpoint using `ResponseEntity<T>` with proper HTTP status codes.

---

## Project Structure
```
src/main/java/com/experiment/crud/
├── CrudApplication.java                  # Entry point
├── DataLoader.java                       # Seeds 15 sample courses on startup
├── entity/
│   └── Course.java                       # JPA entity
├── repository/
│   └── CourseRepository.java             # Spring Data JPA + JPQL search
├── service/
│   └── CourseService.java                # Business logic / CRUD
├── controller/
│   └── CourseController.java             # REST endpoints with ResponseEntity
├── dto/
│   ├── ApiResponse.java                  # Wrapper: { success, message, data, timestamp }
│   └── ErrorResponse.java                # Error wrapper: { status, error, message, path }
└── exception/
    ├── CourseNotFoundException.java
    └── GlobalExceptionHandler.java       # @ControllerAdvice
```

---

## Run the Application
```bash
mvn spring-boot:run
```
- **Server:** `http://localhost:8080`
- **H2 Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:coursedb`
  - Username: `sa` | Password: *(blank)*

---

## ResponseEntity HTTP Status Code Reference
| Operation  | Success         | Failure              |
|------------|-----------------|----------------------|
| POST       | 201 Created     | 400 Bad Request / 409 Conflict |
| GET        | 200 OK          | 404 Not Found        |
| PUT        | 200 OK          | 404 Not Found        |
| DELETE     | 200 OK          | 404 Not Found        |
| Search     | 200 OK          | 404 Not Found        |

---

## Postman Test Guide

All responses are wrapped in:
```json
{
  "success": true,
  "message": "...",
  "data": { ... },
  "timestamp": "2024-01-01T10:00:00"
}
```

---

### ✅ Task 3: POST – Create a Course
**POST** `http://localhost:8080/courses`  
Headers: `Content-Type: application/json`

```json
{
  "title":         "Spring Boot Mastery",
  "instructor":    "John Doe",
  "category":      "Backend",
  "description":   "Complete guide to Spring Boot",
  "durationHours": 30,
  "price":         49.99,
  "level":         "Beginner"
}
```
**201 Created** ✅ | **409 Conflict** (duplicate title) | **400 Bad Request** (missing fields)

---

### ✅ Task 3: GET All Courses
**GET** `http://localhost:8080/courses`  
→ **200 OK** with array of all courses

---

### ✅ Task 3: GET Course by ID
**GET** `http://localhost:8080/courses/1`   → **200 OK**  
**GET** `http://localhost:8080/courses/999` → **404 Not Found**

---

### ✅ Task 3: PUT – Update Course
**PUT** `http://localhost:8080/courses/2`  
*(Only include fields you want to change)*
```json
{
  "price": 39.99,
  "level": "Intermediate"
}
```
→ **200 OK** with updated course | **404 Not Found** if ID missing

---

### ✅ Task 3: DELETE Course
**DELETE** `http://localhost:8080/courses/3` → **200 OK**  
**DELETE** `http://localhost:8080/courses/999` → **404 Not Found**

---

### ✅ Task 5: Search by Title
**GET** `http://localhost:8080/courses/search/spring`  
**GET** `http://localhost:8080/courses/search/docker`  
**GET** `http://localhost:8080/courses/search/xyz123`  → **404 Not Found**

```json
{
  "success": true,
  "message": "2 course(s) found matching 'spring'.",
  "data": [ ... ]
}
```

---

### Extra Endpoints
| URL | Description |
|---|---|
| `GET /courses/category/Backend` | Filter by category |
| `GET /courses/instructor/Alice Kumar` | Filter by instructor |
| `GET /courses/level/Beginner` | Filter by level |
| `GET /courses/active` | Active courses only |
| `GET /courses/filter?minPrice=20&maxPrice=60` | Price range |
| `GET /courses/count` | Total count |

---

## Sample Course Fields
| Field | Type | Validation |
|---|---|---|
| `title` | String | Required, 3–100 chars |
| `instructor` | String | Required |
| `category` | String | Required |
| `description` | String | Optional |
| `durationHours` | Integer | Required, min 1 |
| `price` | Double | Required, min 0 |
| `level` | String | Beginner / Intermediate / Advanced |

---

## GitHub
```bash
git init
git add .
git commit -m "Experiment 7: REST CRUD with ResponseEntity"
git remote add origin <your-repo-url>
git push -u origin main
```
