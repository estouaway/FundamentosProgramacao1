import java.io.*;
import java.util.Arrays;

/**
 * Classe que representa a biblioteca.
 * Gere os livros e autores, e permite adicionar, remover, requisitar e devolver livros.
 * Carrega e guarda as informações dos livros a partir de um ficheiro.
 */
public class Library {

    private String[] authors;  // Array para guardar os autores
    private Book[][] books;    // Array bidimensional para guardar os livros, cada autor tem os seus livros

    /**
     * Construtor da classe Library.
     * Inicializa os arrays de autores e livros e carrega tudo a partir de um ficheiro.
     */
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

    /**
     * Método para carregar os livros a partir de um ficheiro.
     * Se o ficheiro não existir, cria um novo ficheiro vazio.
     *
     * @throws IOException Se ocorrer um erro ao ler o ficheiro ou a criar.
     */
    private void loadBooks() throws IOException {
        File file = new File("BooksFile");

        // Verifica se o ficheiro existe, se não, cria um vazio
        if (!file.exists()) {
            boolean fileCreated = file.createNewFile();
            if (!fileCreated) {
                throw new IOException("Erro a criar ficheiro.");
            }
        }

        String[] tempAuthors = new String[100];
        Book[][] tempBooks = new Book[100][];
        int authorCount = 0;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        // Lê cada linha do ficheiro e processa os dados
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts.length == 3) {
                String author = parts[0].trim();
                String title = parts[1].trim();
                boolean available = Boolean.parseBoolean(parts[2].trim());

                int authorIndex = findAuthorIndex(tempAuthors, author, authorCount);

                if (authorIndex == -1) {
                    // Novo autor encontrado
                    tempAuthors[authorCount] = author;
                    tempBooks[authorCount] = new Book[100];
                    authorIndex = authorCount;
                    authorCount++;
                }

                // Adiciona livro para o autor
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

    /**
     * Método que encontra o índice de um autor no array de autores.
     *
     * @param authors Lista de autores.
     * @param author Nome do autor a procurar.
     * @param count Número de autores atualmente na lista.
     * @return O índice do autor ou -1 se o autor não for encontrado.
     */
    private int findAuthorIndex(String[] authors, String author, int count) {
        for (int i = 0; i < count; i++) {
            if (authors[i].equals(author)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Método que conta quantos livros existem em um array de livros.
     *
     * @param books Array de livros.
     * @return O número de livros no array.
     */
    private int getBookCount(Book[] books) {
        int count = 0;
        for (Book book : books) {
            if (book != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retorna a lista de autores da biblioteca.
     *
     * @return Array com os nomes dos autores.
     */
    public String[] getAuthors() {
        return authors;
    }

    /**
     * Retorna a lista de livros de um autor específico.
     *
     * @param authorIndex Índice do autor na lista de autores.
     * @return Array de livros do autor.
     */
    public Book[] getBooksByAuthor(int authorIndex) {
        return books[authorIndex];
    }

    /**
     * Verifica a disponibilidade de um livro.
     *
     * @param authorIndex Índice do autor na lista de autores.
     * @param bookIndex Índice do livro na lista de livros do autor.
     * @return True se o livro estiver disponível, false caso contrário.
     */
    public boolean getAvailability(int authorIndex, int bookIndex) {
        return books[authorIndex][bookIndex].isAvailable();
    }

    /**
     * Adiciona um novo livro à biblioteca.
     *
     * @param author Nome do autor do livro.
     * @param title Título do livro.
     * @throws IOException Se ocorrer um erro ao salvar os livros no ficheiro.
     */
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

    /**
     * Remove um livro da biblioteca.
     *
     * @param author Nome do autor do livro.
     * @param title Título do livro.
     * @throws IOException Se ocorrer um erro ao salvar os livros no ficheiro.
     */
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

    /**
     * Requisita um livro da biblioteca.
     *
     * @param author Nome do autor do livro.
     * @param title Título do livro.
     * @return True se o livro foi requisitado com sucesso, false caso contrário.
     * @throws IOException Se ocorrer um erro ao salvar os livros no ficheiro.
     */
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

    /**
     * Devolve um livro para a biblioteca.
     *
     * @param author Nome do autor do livro.
     * @param title Título do livro.
     * @throws IOException Se ocorrer um erro ao salvar os livros no ficheiro.
     */
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

    /**
     * Método para salvar os livros no ficheiro.
     * Sobrescreve o conteúdo do ficheiro com a lista atualizada de livros.
     *
     * @throws IOException Se ocorrer um erro ao escrever no ficheiro.
     */
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
