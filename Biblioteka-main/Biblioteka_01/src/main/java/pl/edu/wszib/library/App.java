package pl.edu.wszib.library;

import pl.edu.wszib.library.client.ConsoleInterface;
import pl.edu.wszib.library.database.BookRepository;
import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.service.AuthenticationService;

public class App {
    public static void main(String[] args) {
        UserRepository userRepo = new UserRepository();
        BookRepository bookRepo = new BookRepository();
        pl.edu.wszib.library.database.CategoryRepository categoryRepo = new pl.edu.wszib.library.database.CategoryRepository();
        pl.edu.wszib.library.database.BorrowingRepository borrowingRepo = new pl.edu.wszib.library.database.BorrowingRepository();
        pl.edu.wszib.library.database.ReservationRepository reservationRepo = new pl.edu.wszib.library.database.ReservationRepository();

        AuthenticationService authService = new AuthenticationService(userRepo);

        ConsoleInterface console = new ConsoleInterface(authService, bookRepo, categoryRepo, borrowingRepo, reservationRepo);

        console.start();
    }
}