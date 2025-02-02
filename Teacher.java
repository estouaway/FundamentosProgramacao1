import java.io.IOException;

/**
 * Classe que representa um utilizador do tipo "Professor" no sistema de biblioteca.
 * O Professor pode adicionar e remover livros da biblioteca.
 */
public class Teacher extends User {

    /**
     * Construtor da classe Teacher. Inicializa a instância do professor com a biblioteca.
     *
     * @param library A biblioteca associada ao professor.
     */
    public Teacher(Library library) {
        super(library);
    }

    /**
     * Método que retorna uma mensagem de boas-vindas personalizada para o Professor.
     *
     * @return A mensagem de boas-vindas para o Professor.
     */
    @Override
    public String welcomeMessage() {
        return "Olá Professor!";
    }

    /**
     * Método que permite ao professor adicionar um livro à biblioteca.
     * Lança uma exceção caso ocorra um erro na operação de adição do livro.
     *
     * @param author O autor do livro a ser adicionado.
     * @param title O título do livro a ser adicionado.
     * @throws IOException Se ocorrer um erro durante a adição do livro à biblioteca.
     */
    public void addBook(String author, String title) throws IOException {
        library.addBook(author, title);
    }

    /**
     * Método que permite ao professor remover um livro da biblioteca.
     * Lança uma exceção caso ocorra um erro na operação de remoção do livro.
     *
     * @param author O autor do livro a ser removido.
     * @param title O título do livro a ser removido.
     * @throws IOException Se ocorrer um erro durante a remoção do livro da biblioteca.
     */
    public void removeBook(String author, String title) throws IOException {
        library.removeBook(author, title);
    }
}
