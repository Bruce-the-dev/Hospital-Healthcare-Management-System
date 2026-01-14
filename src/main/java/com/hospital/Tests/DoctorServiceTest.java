package com.hospital.Tests;

import com.hospital.Models.Doctor;
import com.hospital.Service.DoctorService;

public class DoctorServiceTest {

    public static void main(String[] args) {
        DoctorService ds = new DoctorService();

        // ---- Add Doctors ----
        Doctor d1 = new Doctor();
        d1.setFirstName("Alice");
        d1.setLastName("Smith");
        d1.setSpecialization("Cardiology");
        d1.setDepartmentId(1);
        ds.addDoctor(d1);

        Doctor d2 = new Doctor();
        d2.setFirstName("Bob");
        d2.setLastName("Jones");
        d2.setSpecialization("Neurology");
        d2.setDepartmentId(2);
        ds.addDoctor(d2);

        // ---- Fetch & Cache Test ----
        long start1 = System.nanoTime();
        Doctor fetched1 = ds.getDoctorById(d1.getDoctorId());
        long end1 = System.nanoTime();
        System.out.println("DB fetch: " + fetched1 + " Time: " + (end1 - start1));

        long start2 = System.nanoTime();
        Doctor fetched2 = ds.getDoctorById(d1.getDoctorId());
        long end2 = System.nanoTime();
        System.out.println("Cache fetch: " + fetched2 + " Time: " + (end2 - start2));

        // ---- Search ----
        System.out.println("Search 'Smith': " + ds.searchByLastName("Smith"));

        // ---- Sort ----
        System.out.println("Sorted by specialization: " + ds.sortBySpecialization());

        // ---- Update ----
        d1.setSpecialization("General Surgery");
        ds.updateDoctor(d1);
        System.out.println("After update: " + ds.getDoctorById(d1.getDoctorId()));

        // ---- Delete ----
        ds.deleteDoctor(d2.getDoctorId());
        System.out.println("After delete, fetch Bob: " + ds.getDoctorById(d2.getDoctorId()));
    }
}

