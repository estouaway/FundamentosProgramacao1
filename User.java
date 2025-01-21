public abstract class User {

    protected final Library library;
    protected User(Library library) {
        this.library = library;
    }

    protected abstract String welcomeMessage();

    protected String[] listAuthors() {
        return library.getAuthors();
    }

    protected Book[] getBooksByAuthor(int authorIndex) {
        return library.getBooksByAuthor(authorIndex);
    }
}