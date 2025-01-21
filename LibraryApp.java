import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean canOpenGui = false;
        int choice = 0;

        System.out.println("Bem-vindo ao Sistema de Gestão da Biblioteca!");

        // Loop até o user inserir um valor válido
        do {
            System.out.print("Escolha o tipo de utilizador (1 - Professor, 2 - Aluno): ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            }

            if (choice == 1 || choice == 2) {
                canOpenGui = true;
            } else {
                System.out.println("Por favor, insira uma opção válida!");
                scanner.next();
            }

        } while (!canOpenGui);

        // isto o ideal seria um if pq temos apenas if else... podem alterar se quiserem
        switch (choice) {
            case 1 ->
                // Se for Professor, abrirá a interface gráfica com opções de adicionar/remover livros
                    new LibraryGui(new Teacher(new Library()));
            default ->
                // Se for Aluno, abrirá a interface gráfica com a opção de requisitar/devolver livros
                    new LibraryGui(new Student(new Library()));
        }
    }
}
