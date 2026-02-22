package pl.edu.wszib.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:h2:./biblioteka-db";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        init();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE IF NOT EXISTS categories (id IDENTITY PRIMARY KEY, name VARCHAR(255) UNIQUE)");
            s.execute("CREATE TABLE IF NOT EXISTS books (isbn VARCHAR(50) PRIMARY KEY, title VARCHAR(255), author VARCHAR(255), category VARCHAR(255), available BOOLEAN)");
            s.execute("CREATE TABLE IF NOT EXISTS borrowings (id IDENTITY PRIMARY KEY, isbn VARCHAR(50), user_login VARCHAR(100), borrowed_at TIMESTAMP, returned_at TIMESTAMP)");
            s.execute("CREATE TABLE IF NOT EXISTS reservations (id IDENTITY PRIMARY KEY, isbn VARCHAR(50), user_login VARCHAR(100), reserved_at TIMESTAMP)");

            s.execute("MERGE INTO categories (name) KEY(name) VALUES('Uncategorized')");
            s.execute("MERGE INTO categories (name) KEY(name) VALUES('Fantasy')");
            s.execute("MERGE INTO categories (name) KEY(name) VALUES('Sci-Fi')");
            s.execute("MERGE INTO categories (name) KEY(name) VALUES('Kryminał')");
            s.execute("MERGE INTO categories (name) KEY(name) VALUES('Historia')");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1001','Władca Cieni','A. Kowalski','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1002','Smoków Dziedzictwo','B. Nowak','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1003','Królowa Lodu','C. Wiśniewski','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1004','Płomień i Miecz','D. Zielińska','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1005','Las Tajemnic','E. Maj','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1006','Dzieci Gwiazd','F. Sikora','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1007','Księga Run','G. Jankowski','Fantasy', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-1008','Ostatni Mag','H. Lewandowski','Fantasy', TRUE)");

            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2001','Mgławica','I. Bąk','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2002','Stacja Solaris','J. Piotrowski','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2003','Orbita Złudzeń','K. Kaczmarek','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2004','Kolonia 9','L. Ostrowska','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2005','Echo Przyszłości','M. Baran','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2006','Czarna Dziura','N. Szymański','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2007','Wspólny Horyzont','O. Górski','Sci-Fi', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-2008','Pionierzy Kosmosu','P. Krawczyk','Sci-Fi', TRUE)");

            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3001','Cień na Mieście','R. Kowal','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3002','Zagadka Starego Domu','S. Wróblewska','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3003','Ostatni Świadek','T. Król','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3004','Ślady na Piasku','U. Malinowski','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3005','Bezsenność','V. Adamczyk','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3006','Zimny Trop','W. Laskowski','Kryminał', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-3007','Głos z Nocy','X. Domański','Kryminał', TRUE)");

            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4001','Saga Narodów','Y. Nowicka','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4002','Bitwy i Dowódcy','Z. Piasecki','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4003','Starożytne Cywilizacje','A. Wysocki','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4004','Historia Europy','B. Ostapko','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4005','Przeszłość i Pamięć','C. Urban','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4006','Rewolucje','D. Orłowski','Historia', TRUE)");
            s.execute("MERGE INTO books (isbn, title, author, category, available) KEY(isbn) VALUES('978-4007','Miasta-przebudzenia','E. Bielecki','Historia', TRUE)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
