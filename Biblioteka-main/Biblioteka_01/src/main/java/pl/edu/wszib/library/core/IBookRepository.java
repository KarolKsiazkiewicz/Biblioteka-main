package pl.edu.wszib.library.core;

import pl.edu.wszib.library.model.Book;
import java.util.List;
import java.util.Optional;

public interface IBookRepository {
    void save(Book book);
    void delete(String isbn);
    // Metoda void, bo edycja dzieje się wewnątrz
    void update(String isbn, String newTitle, String newAuthor);
    List<Book> findAll();
    List<Book> findByTitleOrAuthor(String query);
    Optional<Book> findByIsbn(String isbn);
}