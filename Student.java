import java.io.IOException;

/**
 * Classe que representa um utilizador do tipo "Estudante" no sistema de biblioteca.
 * O Estudante pode requisitar e devolver livros.
 */
public class Student extends User {

    /**
     * Construtor da classe Student. Inicializa a instância do estudante com a biblioteca.
     *
     * @param library A biblioteca associada ao estudante.
     */
    public Student(Library library) {
        super(library);
    }

    /**
     * Método que retorna uma mensagem de boas-vindas personalizada para o Estudante.
     *
     * @return A mensagem de boas-vindas para o Estudante.
     */
    @Override
    public String welcomeMessage() {
        return "Olá Estudante!";
    }

    /**
     * Método que permite ao estudante requisitar um livro da biblioteca.
     * Lança uma exceção caso ocorra um erro na operação.
     *
     * @param book O livro que o estudante pretende requisitar.
     * @return true se a requisição for bem-sucedida, false caso contrário.
     * @throws IOException Se ocorrer um erro durante a requisição do livro.
     */
    public boolean requestBook(Book book) throws IOException {
        return library.requestBook(book.getAuthor(), book.getTitle());
    }

    /**
     * Método que permite ao estudante devolver um livro à biblioteca.
     * Lança uma exceção caso ocorra um erro na operação.
     *
     * @param book O livro que o estudante deseja devolver.
     * @throws IOException Se ocorrer um erro durante a devolução do livro.
     */
    public void returnBook(Book book) throws IOException {
        library.returnBook(book.getAuthor(), book.getTitle());
    }
}