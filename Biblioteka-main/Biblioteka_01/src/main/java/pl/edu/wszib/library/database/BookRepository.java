package pl.edu.wszib.library.database;

import pl.edu.wszib.library.core.IBookRepository;
import pl.edu.wszib.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements IBookRepository {

    public BookRepository() {
        // seed sample data if not present
        try (Connection c = Database.getConnection(); Statement s = c.createStatement()) {
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1','W pustyni i w puszczy','Henryk Sienkiewicz','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2','Java. Podstawy','Cay S. Horstmann','Programowanie', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3','Czysty Kod','Robert C. Martin','Programowanie', TRUE)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Book book) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(
                "MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES(?,?,?,?,?)")) {
            ps.setString(1, book.getIsbn());
            ps.setString(2, book.getTitle());
            ps.setString(3, book.getAuthor());
            ps.setString(4, book.getCategory());
            ps.setBoolean(5, book.isAvailable());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String isbn) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(String isbn, String newTitle, String newAuthor) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE books SET title = ?, author = ? WHERE isbn = ?")) {
            ps.setString(1, newTitle);
            ps.setString(2, newAuthor);
            ps.setString(3, isbn);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT isbn, title, author, category, available FROM books")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Book> findByTitleOrAuthor(String query) {
        List<Book> list = new ArrayList<>();
        String q = "%" + query.toLowerCase() + "%";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT isbn, title, author, category, available FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?")) {
            ps.setString(1, q);
            ps.setString(2, q);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT isbn, title, author, category, available FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("category"),
                rs.getBoolean("available")
        );
    }
}