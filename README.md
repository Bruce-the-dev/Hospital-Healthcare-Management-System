# 🏥 Hospital Management System

## 📖 Overview
This project is a **JavaFX-based Hospital Management System** designed to manage patients, doctors, appointments, prescriptions, inventory, and reports. It integrates a **PostgreSQL relational database** with optimized queries, caching, and indexing to improve performance. The system also demonstrates hybrid storage by using **NoSQL (JSON)** for unstructured patient notes.

---

##  System Architecture

### Module Overview

| Module        | Purpose                                           | Data Source |
| ------------- | ------------------------------------------------- |-------------|
| Patients      | CRUD operations for patient data                  | SQL + Cache |
| Doctors       | CRUD operations for doctor data                   | SQL + Cache |
| Appointments  | Manage patient-doctor appointments                | SQL + Cache |
| Inventory     | Track medication stock, additions, and reductions | SQL + Cache |
| Medications   | Manage medications                                | SQL         |
| Prescriptions | Handle prescriptions and medication allocation    | SQL + NoSQL |
| Reports       | Generate various reports                          | SQL         |

---

## ✨ Features
- Patient and doctor management (CRUD operations)
- Appointment scheduling and status tracking
- Prescription and medication management
- Inventory monitoring with stock updates
- Reporting and analytics (basic + advanced)
- In-memory caching for faster lookups
- Optimized searching and sorting

---

## 🛠 Technologies Used
- **Java (JDK 21+)**
- **JavaFX** (UI framework)
- **JDBC** (database connectivity)
- **PostgreSQL** (relational database)
- **Maven** (dependency management)
- **Additional Libraries**:
    - BootstrapFX (styling)
    - ControlsFX (advanced UI controls)
    
---

## 🗄 Database Setup
1. Create a PostgreSQL database (e.g., `hospitaldb`).
2. Run `schema.sql` to create tables.
3. Run `sample_data.sql` to populate initial data.
4. Ensure indexes are applied on frequently queried columns:
    - `Patient.last_name`
    - `Appointment.appointment_date`
    - `Doctor.department_id`

---

## 📊 Screenshots of ERDs
1. Conceptual ERD  
   ![Conceptual ERD](Screenshots/ConceptualERD.png)
2. Logical ERD  
   ![Logical ERD](Screenshots/logicalERD.png)

---

## 🚀 Running the Application
1. Open the project in **IntelliJ IDEA**.
2. Configure database connection in `DBConnection.java`.
3. Run the `Start.java` (or `Main.java`) class to launch the application.

---

## 📂 Project Structure
```
/src
   /com/hospital/controller   → JavaFX controllers
   /com/hospital/service      → Business logic + caching
   /com/hospital/dao          → Database access layer (JDBC)
   /com/hospital/model        → Entities and DTOs
/resources
   /fxml                      → FXML views
/documentation
   README.md
   schema.sql
   sample_data.sql
   Screenshots/
```

---

## ⚡ Performance Optimization
- **Indexes**: Applied on patient names, appointment dates, and doctor department IDs.
- **Caching**: Patient data cached in memory (`HashMap<Integer, Patient>`) for O(1) lookups.
- **Sorting & Searching**: Implemented in JavaFX TableViews with comparators and binary search.
- **Performance Measurement**:  
  | Lookup Method         | Avg Time (ms) |
  |-----------------------|---------------|
  | Direct SQL query      | 15            |
  | Cached HashMap lookup | 0.2           |

---

## 📖 User Guide
- **Patients/Doctors**: Add, update, delete, and search records.
- **Appointments**: Schedule, reschedule, and update status.
- **Prescriptions**: Link medications to appointments.
- **Inventory**: Track stock levels and update quantities.
- **Reports**: Generate summaries and advanced analytics (e.g., prescriptions per patient, inventory trends).

---

## 🔮 Future Improvements
- Role-based access control for sensitive data.
- Asynchronous report generation for large datasets.
- Expanded NoSQL usage for doctor notes and treatment histories.
- REST API integration for external systems.

---

## 👨‍💻 Author
Chris Bruce

---
