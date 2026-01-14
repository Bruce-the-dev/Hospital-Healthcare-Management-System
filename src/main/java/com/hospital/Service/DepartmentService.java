package com.hospital.Service;

import com.hospital.Dao.DepartmentDAO;
import com.hospital.Models.Department;

import java.util.*;
import java.util.stream.Collectors;

public class DepartmentService {

    private final DepartmentDAO departmentDAO;
    private final Map<Integer, Department> departmentCache;

    public DepartmentService() {
        this.departmentDAO = new DepartmentDAO();
        this.departmentCache = new HashMap<>();
    }

    // ----------------- CREATE -----------------
    public void addDepartment(Department dept) {
        boolean success = departmentDAO.addDepartment(dept);
        if (success) {
            departmentCache.put(dept.getDepartmentId(), dept);
        }
    }

    // ----------------- READ -----------------
    public Department getDepartmentById(int id) {
        if (departmentCache.containsKey(id)) {
            return departmentCache.get(id);
        }
        Department d = departmentDAO.getDepartmentById(id);
        if (d != null) departmentCache.put(d.getDepartmentId(), d);
        return d;
    }

    public List<Department> getAllDepartments() {
        List<Department> all = departmentDAO.getAllDepartments();
        for (Department d : all) {
            departmentCache.put(d.getDepartmentId(), d);
        }
        return all;
    }

    // ----------------- UPDATE -----------------
    public void updateDepartment(Department dept) {
        boolean success = departmentDAO.updateDepartment(dept);
        if (success) {
            departmentCache.put(dept.getDepartmentId(), dept);
        }
    }

    // ----------------- DELETE -----------------
    public void deleteDepartment(int id) {
        boolean success = departmentDAO.deleteDepartment(id);
        if (success) {
            departmentCache.remove(id);
        }
    }

    // ----------------- SEARCH / SORT -----------------
    public List<Department> searchByName(String name) {
        return getAllDepartments().stream()
                .filter(d -> d.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Department> sortByName() {
        List<Department> list = new ArrayList<>(getAllDepartments());
        list.sort(Comparator.comparing(Department::getName));
        return list;
    }

    // Optional: clear cache
    public void clearCache() {
        departmentCache.clear();
    }
}
