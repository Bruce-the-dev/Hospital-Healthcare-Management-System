package com.hospital.Service;

import com.hospital.Dao.DepartmentDAO;
import com.hospital.Dao.DoctorDAO;
import com.hospital.Models.Department;
import com.hospital.Models.Doctor;

import java.util.*;
import java.util.stream.Collectors;

public class DoctorService {

    private final DoctorDAO doctorDAO;
    private final Map<Integer, Doctor> doctorCache; // In-memory cache
    private final DepartmentDAO dptDao = new DepartmentDAO();

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
        this.doctorCache = new HashMap<>();
    }

    // ----------------- CREATE -----------------
    public void addDoctor(Doctor doctor) {
        Department exist = dptDao.getDepartmentById(doctor.getDepartmentId());
        if (exist != null) {
            boolean success = doctorDAO.addDoctor(doctor);
            if (success) {
                doctorCache.put(doctor.getDoctorId(), doctor); // update cache
            }
        }
    }

    // ----------------- READ -----------------
    public Doctor getDoctorById(int id) {
        // check cache first
        if (doctorCache.containsKey(id)) {
            return doctorCache.get(id);
        }
        Doctor d = doctorDAO.getDoctorById(id);
        if (d != null) doctorCache.put(d.getDoctorId(), d);
        return d;
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> allDoctors = doctorDAO.getAllDoctors();
        // update cache
        for (Doctor d : allDoctors) {
            doctorCache.put(d.getDoctorId(), d);
        }
        return allDoctors;
    }

    // ----------------- UPDATE -----------------
    public void updateDoctor(Doctor doctor) {
        boolean success = doctorDAO.updateDoctor(doctor);
        if (success) {
            doctorCache.put(doctor.getDoctorId(), doctor); // update cache
        }
    }

    // ----------------- DELETE -----------------
    public void deleteDoctor(int id) {
        boolean success = doctorDAO.deleteDoctor(id);
        if (success) {
            doctorCache.remove(id); // invalidate cache
        }
    }

    // ----------------- SEARCH -----------------
    public List<Doctor> searchByLastName(String lastName) {
        return getAllDoctors().stream()
                .filter(d -> d.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Doctor> searchBySpecialization(String specialization) {
        return getAllDoctors().stream()
                .filter(d -> d.getSpecialization().toLowerCase().contains(specialization.toLowerCase()))
                .collect(Collectors.toList());
    }

    // ----------------- SORT -----------------
    public List<Doctor> sortByLastName() {
        List<Doctor> list = new ArrayList<>(getAllDoctors());
        list.sort(Comparator.comparing(Doctor::getLastName));
        return list;
    }

    public List<Doctor> sortBySpecialization() {
        List<Doctor> list = new ArrayList<>(getAllDoctors());
        list.sort(Comparator.comparing(Doctor::getSpecialization));
        return list;
    }

    // Optional: clear cache completely
    public void clearCache() {
        doctorCache.clear();
    }
}
