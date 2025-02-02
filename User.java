/**
 * Classe abstrata que representa um utilizador do sistema de biblioteca.
 * Contém métodos comuns para listar autores e obter livros de um autor específico.
 */
public abstract class User {

    /**
     * Atributo que guarda a instância da biblioteca associada ao utilizador.
     * Permite que o utilizador acesse as funcionalidades da biblioteca.
     */
    protected final Library library;

    /**
     * Construtor da classe User. Inicializa a biblioteca associada ao utilizador.
     *
     * @param library A biblioteca associada ao utilizador.
     */
    protected User(Library library) {
        this.library = library;
    }

    /**
     * Método abstrato que retorna uma mensagem de boas-vindas para o utilizador.
     * Este método será implementado pelas classes filhas para personalizar a mensagem
     * de acordo com o tipo de utilizador.
     *
     * @return A mensagem de boas-vindas do utilizador.
     */
    protected abstract String welcomeMessage();

    /**
     * Método que retorna uma lista de autores disponíveis na biblioteca.
     *
     * @return Um array de strings com os nomes dos autores.
     */
    protected String[] listAuthors() {
        return library.getAuthors();
    }

    /**
     * Método que retorna os livros de um autor específico.
     * Utiliza o índice do autor na lista de autores da biblioteca para obter os livros correspondentes.
     *
     * @param authorIndex O índice do autor na lista de autores.
     * @return Um array de livros do autor especificado.
     */
    protected Book[] getBooksByAuthor(int authorIndex) {
        return library.getBooksByAuthor(authorIndex);
    }
}
