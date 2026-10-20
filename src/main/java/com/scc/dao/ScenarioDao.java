
package com.scc.dao;

import com.scc.model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScenarioDao {

    public void initTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS medicines (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    brand_name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    price REAL NOT NULL CHECK(price >= 0),
                    stock INTEGER NOT NULL CHECK(stock >= 0)
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void addMedicine(Medicine medicine) throws SQLException {
        String sql = """
                INSERT INTO medicines
                (brand_name, category, price, stock)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medicine.brandName());
            ps.setString(2, medicine.category());
            ps.setDouble(3, medicine.price());
            ps.setInt(4, medicine.stock());

            ps.executeUpdate();
        }
    }

    public List<Medicine> getAllRecords() throws SQLException {
        List<Medicine> list = new ArrayList<>();

        String sql = """
                SELECT id, brand_name, category, price, stock
                FROM medicines
                ORDER BY id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Medicine(
                        rs.getInt("id"),
                        rs.getString("brand_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                ));
            }
        }

        return list;
    }

    public List<Medicine> searchMedicine(String keyword)
            throws SQLException {

        List<Medicine> list = new ArrayList<>();

        String sql = """
                SELECT id, brand_name, category, price, stock
                FROM medicines
                WHERE brand_name LIKE ? OR category LIKE ?
                ORDER BY id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword + "%";
            ps.setString(1, search);
            ps.setString(2, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Medicine(
                            rs.getInt("id"),
                            rs.getString("brand_name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    ));
                }
            }
        }

        return list;
    }

    public boolean updateMedicine(Medicine medicine)
            throws SQLException {

        String sql = """
                UPDATE medicines
                SET brand_name = ?, category = ?, price = ?, stock = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medicine.brandName());
            ps.setString(2, medicine.category());
            ps.setDouble(3, medicine.price());
            ps.setInt(4, medicine.stock());
            ps.setInt(5, medicine.id());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteMedicine(int id) throws SQLException {
        String sql = "DELETE FROM medicines WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
