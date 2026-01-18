CREATE TABLE Department (
    department_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100)
);
CREATE TABLE Doctor (
    doctor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialization VARCHAR(100),
    department_id INT REFERENCES Department(department_id) ON DELETE SET NULL
);
CREATE TABLE Patient (
    patient_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender CHAR(1),
    date_of_birth DATE,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);
CREATE TABLE Appointment (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES Patient(patient_id) ON DELETE CASCADE,
    doctor_id INT REFERENCES Doctor(doctor_id) ON DELETE CASCADE,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(20)
);
CREATE TABLE Prescription (
    prescription_id SERIAL PRIMARY KEY,
    appointment_id INT REFERENCES Appointment(appointment_id) ON DELETE CASCADE,
    issued_date DATE NOT NULL,
    notes TEXT
);
CREATE TABLE Medication (
    medication_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);
CREATE TABLE Prescription_Medication (
    prescription_id INT REFERENCES Prescription(prescription_id) ON DELETE CASCADE,
    medication_id INT REFERENCES Medication(medication_id) ON DELETE CASCADE,
    dosage VARCHAR(50),
    PRIMARY KEY (prescription_id, medication_id)
);
CREATE TABLE Inventory (
    medication_id INT PRIMARY KEY REFERENCES Medication(medication_id) ON DELETE CASCADE,
    quantity INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT NOW()
);
CREATE TABLE Feedback (
    feedback_id SERIAL PRIMARY KEY,
    patient_id INT REFERENCES Patient(patient_id) ON DELETE CASCADE,
    doctor_id INT REFERENCES Doctor(doctor_id) ON DELETE CASCADE,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT
);

CREATE INDEX idx_patient_lastname ON Patient(last_name);
CREATE INDEX idx_appointment_date ON Appointment(appointment_date);
CREATE INDEX idx_doctor_department ON Doctor(department_id);
