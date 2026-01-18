-- ----------------------------
-- Sample Patients
-- ----------------------------
INSERT INTO Patient (first_name, last_name, date_of_birth, gender)
VALUES
('John', 'Doe', '1990-05-12', 'Male'),
('Jane', 'Smith', '1985-11-03', 'Female'),
('Alice', 'Johnson', '2000-02-20', 'Female');

-- ----------------------------
-- Sample Doctors
-- ----------------------------
INSERT INTO Doctor (first_name, last_name, specialization, department_id)
VALUES
('Emily', 'Brown', 'Cardiology', 1),
('Michael', 'Green', 'Neurology', 2);

-- ----------------------------
-- Sample Departments
-- ----------------------------
INSERT INTO Department (name)
VALUES ('Cardiology'), ('Neurology'), ('Pediatrics');

-- ----------------------------
-- Sample Medications
-- ----------------------------
INSERT INTO Medication (name, description)
VALUES
('Paracetamol', 'Pain reliever and fever reducer'),
('Amoxicillin', 'Antibiotic for bacterial infections');

-- ----------------------------
-- Sample Inventory
-- ----------------------------
INSERT INTO Inventory (medication_id, quantity)
VALUES
(1, 100),
(2, 50);

-- ----------------------------
-- Sample Appointments
-- ----------------------------
INSERT INTO Appointment (patient_id, doctor_id, appointment_date, status)
VALUES
(1, 1, '2026-01-20 09:00:00', 'Pending'),
(2, 2, '2026-01-21 10:00:00', 'Pending');

-- ----------------------------
-- Sample Prescriptions
-- ----------------------------
INSERT INTO Prescription (appointment_id, issued_date, notes)
VALUES
(1, '2026-01-20', 'Take medications as prescribed');

-- ----------------------------
-- Sample PrescriptionMedications
-- ----------------------------
INSERT INTO PrescriptionMedication (prescription_id, medication_id, dosage)
VALUES
(1, 1, '500mg twice daily');
