package pl.edu.wszib.library.model;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private boolean available = true;

    public Book() {}

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = "Uncategorized";
        this.available = true;
    }

    public Book(String isbn, String title, String author, String category, boolean available) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return isbn + " | " + title + " | " + author + " | " + category + " | " + (available ? "Dostępna" : "Wypożyczona");
    }
}