import java.io.IOException;

public class Student extends User {


    public Student(Library library) {
        super(library);
    }

    @Override
    public String welcomeMessage() {
        return "Olá Estudante!";
    }

    public boolean requestBook(Book book) throws IOException {
        return library.requestBook(book.getAuthor(), book.getTitle());
    }

    public void returnBook(Book book) throws IOException {
        library.returnBook(book.getAuthor(), book.getTitle());
    }
}