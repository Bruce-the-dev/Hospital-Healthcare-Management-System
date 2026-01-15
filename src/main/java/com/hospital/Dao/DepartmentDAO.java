package com.hospital.Dao;

import com.hospital.Models.Department;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public boolean addDepartment(Department d) {
        String sql = "INSERT INTO Department(name,location) VALUES (?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getName());
            ps.setString(2,d.getLocation());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                d.setDepartmentId(rs.getInt(1));
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDepartment(int id) {
        String sql = "Delete From Department where department_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Department> getAllDepartments() {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT * FROM Department";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Department d = new Department();
                d.setDepartmentId(rs.getInt("department_id"));
                d.setName(rs.getString("name"));
                d.setLocation(rs.getString("location"));
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateDepartment(Department dept) {
        String sql = "UPDATE Department SET Name=?, location=? " +
                "WHERE department_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dept.getName());
            ps.setString(2, dept.getLocation());
            ps.setInt(3,dept.getDepartmentId());
            int rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Department getDepartmentById(int id) {
        String sql = "Select * from Department WHERE department_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Department dept = new Department();
            if (rs.next()) {
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setName(rs.getString("name"));
                dept.setLocation(rs.getString("location"));
            }
            return dept;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
