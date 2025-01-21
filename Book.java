public class Book {
    private final String title;
    private String author;
    private boolean isAvailable;

    public Book(String title, boolean isAvailable) {
        this.title = title;
        this.isAvailable = isAvailable;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return title + " - " + (isAvailable() ? "Disponível" : "Indisponível");
    }
}
