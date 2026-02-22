package pl.edu.wszib.library.database;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public void reserveBook(String userLogin, String isbn) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO reservations(isbn, user_login, reserved_at) VALUES(?,?,?)")) {
            ps.setString(1, isbn);
            ps.setString(2, userLogin);
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findReservationsForIsbn(String isbn) {
        List<String> list = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT user_login FROM reservations WHERE isbn = ? ORDER BY reserved_at")) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("user_login"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void removeReservation(String userLogin, String isbn) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM reservations WHERE isbn = ? AND user_login = ?")) {
            ps.setString(1, isbn);
            ps.setString(2, userLogin);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
