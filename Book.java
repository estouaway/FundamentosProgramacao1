/**
 * Classe que representa um livro na biblioteca.
 * Contém atributos como título, autor e disponibilidade do livro.
 */
public class Book {

    /**
     * Atributo que guarda o título do livro.
     */
    private String title;

    /**
     * Atributo que guarda o nome do autor do livro.
     */
    private String author;

    /**
     * Atributo que indica se o livro está disponível para empréstimo.
     * (true = disponível, false = indisponível)
     */
    private boolean isAvailable;

    /**
     * Construtor que inicializa o título e a disponibilidade do livro.
     * O autor será configurado posteriormente.
     *
     * @param title O título do livro.
     * @param isAvailable A disponibilidade do livro (true = disponível, false = indisponível).
     */
    public Book(String title, boolean isAvailable) {
        this.title = title;
        this.isAvailable = isAvailable;
    }

    /**
     * Método que retorna o título do livro.
     *
     * @return O título do livro.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Método que retorna o nome do autor do livro.
     *
     * @return O nome do autor do livro.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Método que verifica se o livro está disponível para empréstimo.
     *
     * @return true se o livro estiver disponível, false caso contrário.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Método que define o nome do autor do livro.
     *
     * @param author O nome do autor a ser atribuído ao livro.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Método que define a disponibilidade do livro.
     *
     * @param available A disponibilidade do livro (true = disponível, false = indisponível).
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    /**
     * Método que retorna uma representação em texto do livro.
     * A representação inclui o título e a disponibilidade.
     *
     * @return Uma string com o título e a disponibilidade do livro.
     */
    @Override
    public String toString() {
        return title + " - " + (isAvailable() ? "Disponível" : "Indisponível");
    }
}
