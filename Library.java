import java.io.*;
import java.util.Arrays;

public class Library {

    private String[] authors;  // Array para guardar os autores
    private Book[][] books;    // 2D Array para guardar os livros, cada autor tem os seus livros

    // Constructor
    public Library() {
        authors = new String[0];
        books = new Book[0][0];
        try {
            loadBooks();
        } catch (IOException e) {
            System.err.println("Erro ao carregar livros: " + e.getMessage());
            System.exit(1);
        }
    }

    // Método para carregar os livros a partir de um ficheiro
    private void loadBooks() throws IOException {
        File file = new File("BooksFile");

        // Check if the file exists, if not, create an empty one
        if (!file.exists()) {
            file.createNewFile();
        }

        String[] tempAuthors = new String[100];
        Book[][] tempBooks = new Book[100][];
        int authorCount = 0;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                String author = parts[0].trim();
                String title = parts[1].trim();
                boolean available = Boolean.parseBoolean(parts[2].trim());

                int authorIndex = findAuthorIndex(tempAuthors, author, authorCount);

                if (authorIndex == -1) {
                    // New author found
                    tempAuthors[authorCount] = author;
                    tempBooks[authorCount] = new Book[100];
                    authorIndex = authorCount;
                    authorCount++;
                }

                // Add book for the author
                int bookCount = getBookCount(tempBooks[authorIndex]);
                tempBooks[authorIndex][bookCount] = new Book(title, available);
            }
        }
        reader.close();

        authors = new String[authorCount];
        books = new Book[authorCount][];

        for (int i = 0; i < authorCount; i++) {
            authors[i] = tempAuthors[i];
            books[i] = new Book[getBookCount(tempBooks[i])];
            System.arraycopy(tempBooks[i], 0, books[i], 0, books[i].length);
        }
    }

    private int findAuthorIndex(String[] authors, String author, int count) {
        for (int i = 0; i < count; i++) {
            if (authors[i].equals(author)) {
                return i;
            }
        }
        return -1;
    }

    private int getBookCount(Book[] books) {
        int count = 0;
        for (Book book : books) {
            if (book != null) {
                count++;
            }
        }
        return count;
    }

    public String[] getAuthors() {
        return authors;
    }

    public Book[] getBooksByAuthor(int authorIndex) {
        return books[authorIndex];
    }

    public boolean getAvailability(int authorIndex, int bookIndex) {
        return books[authorIndex][bookIndex].isAvailable();
    }

    public void addBook(String author, String title) throws IOException {
        int authorIndex = findAuthorIndex(authors, author, authors.length);
        if (authorIndex == -1) {
            authorIndex = authors.length;
            authors = Arrays.copyOf(authors, authors.length + 1);
            authors[authorIndex] = author;
            books = Arrays.copyOf(books, books.length + 1);
            books[authorIndex] = new Book[1];
        }

        int bookCount = getBookCount(books[authorIndex]);
        books[authorIndex] = Arrays.copyOf(books[authorIndex], bookCount + 1);
        books[authorIndex][bookCount] = new Book(title, true);

        saveBooksToFile();
    }

    public void removeBook(String author, String title) throws IOException {
        int authorIndex = findAuthorIndex(authors, author, authors.length);
        if (authorIndex == -1) {
            return;
        }

        int bookIndex = -1;
        for (int i = 0; i < books[authorIndex].length; i++) {
            if (books[authorIndex][i].getTitle().equals(title)) {
                bookIndex = i;
                break;
            }
        }

        if (bookIndex == -1) {
            return;
        }

        for (int i = bookIndex; i < books[authorIndex].length - 1; i++) {
            books[authorIndex][i] = books[authorIndex][i + 1];
        }

        books[authorIndex] = Arrays.copyOf(books[authorIndex], books[authorIndex].length - 1);

        if (books[authorIndex].length == 0) {
            for (int i = authorIndex; i < authors.length - 1; i++) {
                authors[i] = authors[i + 1];
                books[i] = books[i + 1];
            }
            authors = Arrays.copyOf(authors, authors.length - 1);
            books = Arrays.copyOf(books, books.length - 1);
        }

        saveBooksToFile();
    }

    public boolean requestBook(String author, String title) throws IOException {
        int authorIndex = findAuthorIndex(authors, author, authors.length);
        if (authorIndex == -1) {
            System.out.println("Autor não encontrado.");
            return false;
        }

        int bookIndex = -1;
        for (int i = 0; i < books[authorIndex].length; i++) {
            if (books[authorIndex][i].getTitle().equals(title)) {
                bookIndex = i;
                break;
            }
        }

        if (bookIndex == -1) {
            System.out.println("Livro não encontrado.");
            return false;
        }

        if (!books[authorIndex][bookIndex].isAvailable()) {
            System.out.println("Livro indisponível.");
            return false;
        }

        books[authorIndex][bookIndex].setAvailable(false);
        System.out.println("Livro '" + title + "' de " + author + " foi requisitado com sucesso.");

        saveBooksToFile();

        return true;
    }

    public void returnBook(String author, String title) throws IOException {
        int authorIndex = findAuthorIndex(authors, author, authors.length);
        if (authorIndex == -1) {
            return;
        }

        int bookIndex = -1;
        for (int i = 0; i < books[authorIndex].length; i++) {
            if (books[authorIndex][i].getTitle().equals(title)) {
                bookIndex = i;
                break;
            }
        }

        if (bookIndex == -1) {
            return;
        }

        if (books[authorIndex][bookIndex].isAvailable()) {
            System.out.println("Livro já estava devolvido.");
            return;
        }

        books[authorIndex][bookIndex].setAvailable(true);

        saveBooksToFile();
    }

    private void saveBooksToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("BooksFile"))) {
            for (int i = 0; i < authors.length; i++) {
                for (Book book : books[i]) {
                    writer.write(authors[i] + ";" + book.getTitle() + ";" + book.isAvailable() + "\n");
                }
            }
        }
    }
}
