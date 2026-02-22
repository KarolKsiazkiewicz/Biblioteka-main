package pl.edu.wszib.library.client;

import pl.edu.wszib.library.core.IAuthenticationService;
import pl.edu.wszib.library.core.IBookRepository;
import pl.edu.wszib.library.database.BorrowingRepository;
import pl.edu.wszib.library.database.CategoryRepository;
import pl.edu.wszib.library.database.ReservationRepository;
import pl.edu.wszib.library.model.Book;
import pl.edu.wszib.library.model.Role;
import pl.edu.wszib.library.model.User;
import java.util.List;
import java.util.Scanner;

public class ConsoleInterface {
    private final IAuthenticationService authService;
    private final IBookRepository bookRepo;
    private final CategoryRepository categoryRepo;
    private final BorrowingRepository borrowingRepo;
    private final ReservationRepository reservationRepo;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public ConsoleInterface(IAuthenticationService authService, IBookRepository bookRepo,
                            CategoryRepository categoryRepo, BorrowingRepository borrowingRepo,
                            ReservationRepository reservationRepo) {
        this.authService = authService;
        this.bookRepo = bookRepo;
        this.categoryRepo = categoryRepo;
        this.borrowingRepo = borrowingRepo;
        this.reservationRepo = reservationRepo;
    }

    public void start() {
        System.out.println("=== SYSTEM BIBLIOTECZNY ===");
        while (true) {
            if (currentUser == null) {
                loginMenu();
            } else {
                mainMenu();
            }
        }
    }

    private void loginMenu() {
        System.out.println("\n1. Zaloguj\n2. Wyjdź");
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Hasło: ");
            String password = scanner.nextLine();

            currentUser = authService.authenticate(login, password);
            if (currentUser != null) {
                System.out.println("Witaj " + currentUser.getLogin());
            } else {
                System.out.println("Błąd logowania!");
            }
        } else if ("2".equals(choice)) {
            System.exit(0);
        }
    }

    private void mainMenu() {
        System.out.println("\n1. Lista książek");
        System.out.println("2. Szukaj");
        System.out.println("3. Moje wypożyczenia");
        System.out.println("4. Wypożycz książkę");
        System.out.println("5. Zwróć książkę");
        System.out.println("6. Zarezerwuj książkę");
        System.out.println("7. Filtrowanie po kategorii");
        System.out.println("8. Wyloguj");

        if (currentUser.getRole() == Role.ADMIN) {
            System.out.println("9. [ADMIN] Dodaj książkę");
            System.out.println("10. [ADMIN] Usuń książkę");
            System.out.println("11. [ADMIN] Edytuj książkę");
            System.out.println("12. [ADMIN] Zarządzaj kategorie");
        }

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> bookRepo.findAll().forEach(System.out::println);
            case "2" -> {
                System.out.print("Szukaj (tytuł/autor): ");
                bookRepo.findByTitleOrAuthor(scanner.nextLine()).forEach(System.out::println);
            }
            case "3" -> borrowingRepo.findActiveByUser(currentUser.getLogin()).forEach(System.out::println);
            case "4" -> borrowBook();
            case "5" -> returnBook();
            case "6" -> reserveBook();
            case "7" -> filterByCategory();
            case "8" -> currentUser = null;
            case "9" -> { if (isAdmin()) addBook(); }
            case "10" -> { if (isAdmin()) removeBook(); }
            case "11" -> { if (isAdmin()) editBook(); }
            case "12" -> { if (isAdmin()) manageCategories(); }
            default -> System.out.println("Nieznana opcja");
        }
    }

    private void addBook() {
        System.out.print("ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Tytuł: "); String title = scanner.nextLine();
        System.out.print("Autor: "); String author = scanner.nextLine();
        System.out.print("Kategoria: "); String category = scanner.nextLine();
        bookRepo.save(new Book(isbn, title, author, category, true));
        System.out.println("Dodano.");
    }

    private void removeBook() {
        System.out.print("Podaj ISBN: ");
        bookRepo.delete(scanner.nextLine());
        System.out.println("Usunięto.");
    }

    private void editBook() {
        System.out.print("Podaj ISBN: "); String isbn = scanner.nextLine();
        System.out.print("Nowy tytuł: "); String title = scanner.nextLine();
        System.out.print("Nowy autor: "); String author = scanner.nextLine();
        bookRepo.update(isbn, title, author);
        System.out.println("Zaktualizowano.");
    }

    private void borrowBook() {
        System.out.print("Podaj ISBN do wypożyczenia: ");
        String isbn = scanner.nextLine();
        boolean ok = borrowingRepo.borrowBook(currentUser.getLogin(), isbn);
        System.out.println(ok ? "Wypożyczono." : "Nie można wypożyczyć (brak dostępności).");
    }

    private void returnBook() {
        System.out.print("Podaj ISBN do zwrotu: ");
        String isbn = scanner.nextLine();
        boolean ok = borrowingRepo.returnBook(currentUser.getLogin(), isbn);
        System.out.println(ok ? "Zwrócono." : "Nie znaleziono aktywnego wypożyczenia.");
    }

    private void reserveBook() {
        System.out.print("Podaj ISBN do rezerwacji: ");
        String isbn = scanner.nextLine();
        reservationRepo.reserveBook(currentUser.getLogin(), isbn);
        System.out.println("Zarezerwowano.");
    }

    private void filterByCategory() {
        List<String> cats = categoryRepo.findAll();
        System.out.println("Dostępne kategorie:");
        for (int i = 0; i < cats.size(); i++) System.out.println((i+1) + ". " + cats.get(i));
        System.out.print("Wybierz numer kategorii: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine()) - 1;
            if (idx >= 0 && idx < cats.size()) {
                String cat = cats.get(idx);
                bookRepo.findAll().stream().filter(b -> cat.equals(b.getCategory())).forEach(System.out::println);
            } else System.out.println("Nieprawidłowy wybór.");
        } catch (NumberFormatException e) { System.out.println("Nieprawidłowy wybór."); }
    }

    private void manageCategories() {
        System.out.println("1. Lista kategorii\n2. Dodaj\n3. Edytuj\n4. Usuń");
        String c = scanner.nextLine();
        switch (c) {
            case "1" -> categoryRepo.findAll().forEach(System.out::println);
            case "2" -> {
                System.out.print("Nazwa kategorii: "); String name = scanner.nextLine(); categoryRepo.add(name); System.out.println("Dodano.");
            }
            case "3" -> {
                System.out.print("Stara nazwa: "); String oldName = scanner.nextLine();
                System.out.print("Nowa nazwa: "); String newName = scanner.nextLine();
                categoryRepo.update(oldName, newName); System.out.println("Zaktualizowano.");
            }
            case "4" -> {
                System.out.print("Nazwa do usunięcia: "); String name = scanner.nextLine(); categoryRepo.delete(name); System.out.println("Usunięto.");
            }
            default -> System.out.println("Nieznana opcja");
        }
    }

    private boolean isAdmin() {
        if (currentUser.getRole() == Role.ADMIN) return true;
        System.out.println("Brak uprawnień!");
        return false;
    }
}