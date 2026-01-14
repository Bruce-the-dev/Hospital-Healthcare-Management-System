package com.hospital.Models.DTO;

import com.hospital.Models.Department;
import com.hospital.Models.Doctor;
import com.hospital.Models.Patient;
import com.hospital.Service.DepartmentService;
import com.hospital.Service.DoctorService;
import com.hospital.Service.PatientService;

public class BootstrapDataLoader {

    private final DepartmentService departmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public BootstrapDataLoader() {
        this.departmentService = new DepartmentService();
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();
    }

    public void loadInitialData() {

        System.out.println("---- Bootstrapping Hospital Data ----");

        // ----------------- DEPARTMENTS -----------------
        Department cardiology = new Department();
        cardiology.setName("Cardiology");
        departmentService.addDepartment(cardiology);

        Department neurology = new Department();
        neurology.setName("Neurology");
        departmentService.addDepartment(neurology);

        Department pediatrics = new Department();
        pediatrics.setName("Pediatrics");
        departmentService.addDepartment(pediatrics);

        System.out.println("Departments loaded");

        // ----------------- DOCTORS -----------------
        Doctor d1 = new Doctor();
        d1.setFirstName("Alice");
        d1.setLastName("Smith");
        d1.setSpecialization("Cardiologist");
        d1.setDepartmentId(cardiology.getDepartmentId());
        doctorService.addDoctor(d1);

        Doctor d2 = new Doctor();
        d2.setFirstName("Bob");
        d2.setLastName("Jones");
        d2.setSpecialization("Neurologist");
        d2.setDepartmentId(neurology.getDepartmentId());
        doctorService.addDoctor(d2);

        System.out.println("Doctors loaded");

        // ----------------- PATIENTS -----------------
        Patient p1 = new Patient();
        p1.setFirstName("John");
        p1.setLastName("Doe");
        p1.setGender('M');
        p1.setEmail("john.doe@gmail.com");
        patientService.addPatient(p1);

        Patient p2 = new Patient();
        p2.setFirstName("Jane");
        p2.setLastName("Brown");
        p2.setGender('F');
        p2.setEmail("jane.brown@gmail.com");
        patientService.addPatient(p2);

        System.out.println("Patients loaded");

        System.out.println("---- Bootstrap completed successfully ----");
    }
}

