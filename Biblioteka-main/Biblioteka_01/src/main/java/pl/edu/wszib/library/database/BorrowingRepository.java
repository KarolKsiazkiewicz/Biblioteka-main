package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.Book;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository {

    public boolean borrowBook(String userLogin, String isbn) {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement check = c.prepareStatement("SELECT available FROM books WHERE isbn = ? FOR UPDATE")) {
                check.setString(1, isbn);
                ResultSet rs = check.executeQuery();
                if (!rs.next() || !rs.getBoolean("available")) {
                    c.rollback();
                    return false;
                }
                try (PreparedStatement borrow = c.prepareStatement("INSERT INTO borrowings(isbn, user_login, borrowed_at) VALUES(?,?,?)")) {
                    borrow.setString(1, isbn);
                    borrow.setString(2, userLogin);
                    borrow.setTimestamp(3, Timestamp.from(Instant.now()));
                    borrow.executeUpdate();
                }
                try (PreparedStatement setAvail = c.prepareStatement("UPDATE books SET available = FALSE WHERE isbn = ?")) {
                    setAvail.setString(1, isbn);
                    setAvail.executeUpdate();
                }
                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean returnBook(String userLogin, String isbn) {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement find = c.prepareStatement("SELECT id FROM borrowings WHERE isbn = ? AND user_login = ? AND returned_at IS NULL FOR UPDATE")) {
                find.setString(1, isbn);
                find.setString(2, userLogin);
                ResultSet rs = find.executeQuery();
                if (!rs.next()) {
                    c.rollback();
                    return false;
                }
                long id = rs.getLong("id");
                try (PreparedStatement ret = c.prepareStatement("UPDATE borrowings SET returned_at = ? WHERE id = ?")) {
                    ret.setTimestamp(1, Timestamp.from(Instant.now()));
                    ret.setLong(2, id);
                    ret.executeUpdate();
                }
                try (PreparedStatement setAvail = c.prepareStatement("UPDATE books SET available = TRUE WHERE isbn = ?")) {
                    setAvail.setString(1, isbn);
                    setAvail.executeUpdate();
                }
                // If there are reservations for this book, leave them; client can handle notifying.
                c.commit();
                return true;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findActiveByUser(String userLogin) {
        List<Book> list = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT b.isbn, b.title, b.author, b.category, b.available FROM borrowings br JOIN books b ON br.isbn = b.isbn WHERE br.user_login = ? AND br.returned_at IS NULL")) {
            ps.setString(1, userLogin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Book(rs.getString("isbn"), rs.getString("title"), rs.getString("author"), rs.getString("category"), rs.getBoolean("available")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
