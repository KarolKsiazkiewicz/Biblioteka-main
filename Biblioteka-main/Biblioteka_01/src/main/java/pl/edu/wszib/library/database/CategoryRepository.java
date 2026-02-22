package pl.edu.wszib.library.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public List<String> findAll() {
        List<String> list = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT name FROM categories ORDER BY name")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void add(String name) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO categories(name) VALUES(?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String name) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM categories WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String oldName, String newName) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE categories SET name = ? WHERE name = ?")) {
            ps.setString(1, newName);
            ps.setString(2, oldName);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
