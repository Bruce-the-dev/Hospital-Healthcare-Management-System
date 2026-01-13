package com.hospital.Dao;

import com.hospital.Models.Department;
import com.hospital.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    public boolean addDepartment(Department d) {
        String sql = "INSERT INTO Department(name) VALUES (?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getName());
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
                list.add(d);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
