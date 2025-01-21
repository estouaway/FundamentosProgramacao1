import java.io.IOException;

public class Teacher extends User {

    public Teacher(Library library) {
        super(library);
    }

    @Override
    public String welcomeMessage() {
        return "Olá Professor!";
    }

    public void addBook(String author, String title) throws IOException {
        library.addBook(author, title);
    }

    public void removeBook(String author, String title) throws IOException {
        library.removeBook(author, title);
    }
}