import java.util.Scanner;

/**
 * Classe principal que inicia o Sistema de Gestão da Biblioteca.
 * Permite ao utilizador escolher entre dois tipos de utilizadores (Professor ou Aluno)
 * e depois abre a interface gráfica correspondente.
 */
public class LibraryApp {

    /**
     * Método principal que executa o programa em modo consola.
     * Permite que o utilizador escolha o tipo de utilizador e, dependendo da escolha,
     * abre a interface gráfica apropriada.
     *
     * @param args Argumentos da linha de comando (não utilizados neste caso).
     */
    public static void main(String[] args) {
        // Scanner utilizado para apanhar entradas do utilizador no terminal.
        Scanner scanner = new Scanner(System.in);

        // Variável que determina se a interface gráfica pode ser aberta.
        boolean canOpenGui = false;

        // Variável que guarda a escolha do utilizador (1 - Professor, 2 - Aluno).
        int choice = 0;

        // Mostra uma mensagem de boas-vindas no terminal.
        System.out.println("Bem-vindo ao Sistema de Gestão da Biblioteca!");

        // Loop até o utilizador inserir um valor válido para o tipo de utilizador.
        do {
            System.out.print("Escolha o tipo de utilizador (1 - Professor, 2 - Aluno): ");

            // Verifica se a entrada é um número inteiro.
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }

            // Verifica se a escolha é válida (1 ou 2).
            if (choice == 1 || choice == 2) {
                canOpenGui = true; // Permite a abertura da interface gráfica se a escolha for válida.
            } else {
                // Caso a escolha não seja válida, exibe uma mensagem de erro e solicita uma nova escolha.
                System.out.println("Por favor, insira uma opção válida!");
                scanner.next(); // Descarta o valor inválido.
            }

            // Continua o loop enquanto a entrada não for válida.
        } while (!canOpenGui);

        // Dependendo da escolha do utilizador, abre a interface gráfica apropriada.
        // Se o utilizador for um Professor (1), abre a interface para adicionar/remover livros.
        // Se o utilizador for um Aluno (2), abre a interface para requisitar/devolver livros.
        switch (choice) {
            case 1 ->
                // Se for Professor, cria a interface gráfica com opções de adicionar/remover livros.
                    new LibraryGui(new Teacher(new Library()));
            default ->
                // Se for Aluno, cria a interface gráfica com a opção de requisitar/devolver livros.
                    new LibraryGui(new Student(new Library()));
        }
    }
}
