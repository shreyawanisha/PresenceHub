# PresenceHub - Attendance Management System

**PresenceHub** is a full-stack web-based **Attendance Management System** designed for educational institutions to streamline student attendance tracking. The application provides role-based functionalities for Admins, Faculty, and Students, including QR-based attendance marking, detailed reporting, and secure authentication.

---

## Features

### Admin
- Authorize Students and Faculty accounts
- Create and manage Courses
- Assign Faculty to Courses
- View all Students, Faculties, and Courses
- View and export Attendance Reports with filters (status/date/student)

### Faculty
- View assigned Courses
- Mark and update Attendance manually or via QR Code
- Generate time-limited QR Codes for attendance (refreshes every 5 minutes)
- View and export attendance reports with filters

### Student
- Register and login
- Self-enroll into Courses (limit of 2 courses)
- Scan QR Codes to mark attendance
- View Attendance Summary and Detailed Records

---

##  Security

- **Authentication**: Implemented using **JWT (JSON Web Tokens)**
  - Stored in **HttpOnly Cookies** for backend validation
  - Also stored in **localStorage** for frontend use (conditional UI rendering)

- **Authorization**:
  - Role-based access via `@PreAuthorize` annotations and Spring Security
  - Roles include: `ADMIN`, `FACULTY`, `STUDENT`
  - Endpoints are secured and validated based on role and JWT claims

---

## Tech Stack

- **Backend**: Spring Boot, Hibernate, JWT
- **Frontend**: JSP, Bootstrap, AJAX (token-based secure API calls)
- **Database**: MySQL
- **Build Tool**: Maven
- **PDF Export**: iText 5.5.13
- **QR Code Generation**: `QRCodeWriter` from ZXing

---

## Key Screens (Highlights)

1. Home Page  
2. Registration (Faculty/Student with validations)  
3. Login  
4. Admin Dashboard  
   - Authorize Students/Faculty  
   - Add Courses  
   - Assign Faculty to Courses  
   - Attendance Reporting & Export  
5. Faculty Dashboard  
   - Manual Attendance  
   - QR Code Generation  
   - Attendance Report View  
6. Student Dashboard  
   - Enroll Courses  
   - Scan QR Code to Mark Attendance  
   - View Attendance Summary & Details  

---

## Project Setup

### 1. Clone the Repository
```bash
git clone https://github.com/shreyawanisha/PresenceHub.git
cd presencehub
```

### 2. Configure the Database
Update `application.properties` with your DB credentials:
```properties
db.url=jdbc:mysql://localhost:3306/presencehub
db.username=yourUsername
db.password=yourPassword
```

### 3. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access the Application
Visit: `http://localhost:8080`

---

## Report & Documentation

For detailed architecture, UI snapshots, and project flow, refer to:  
`ShreyaWanisha_PresenceHub_ProjectReport.pdf`

---

## Author

**Shreya Wanisha**  
Graduate Student, Northeastern University 
🔗 [LinkedIn](https://www.linkedin.com/in/shreya-wanisha)

