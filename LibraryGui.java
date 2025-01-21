import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class LibraryGui {
    private final JFrame frame;
    private final User user;

    private JList<String> authorList;
    private JList<Book> bookList;
    private DefaultListModel<Book> bookListModel;

    public LibraryGui(User user) {
        this.user = user;

        frame = new JFrame("Gestão de Biblioteca");
        frame.setAlwaysOnTop(true);
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);

        placeComponents(panel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        JPanel topPanel = new JPanel(new BorderLayout());

        // Configurar o label de boas-vindas
        JLabel welcomeLabel = new JLabel(user.welcomeMessage());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Criar e posicionar o label "Acerca(F1)" à direita
        JLabel aboutLabel = new JLabel("Acerca(F1)");
        aboutLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(aboutLabel, BorderLayout.EAST);

        // Adicionar o painel ao topo
        panel.add(topPanel, BorderLayout.NORTH);

        // Adicionar o evento de tecla para capturar F1
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "showAboutMessage");
        panel.getActionMap().put("showAboutMessage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panel, "Aplicação de Biblioteca - Versão 1.0\nCriado por: \nCarlos Dias\nJosé Rua\nJoão Gonçalo Antunes", "Acerca", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Criar listas para autores e livros
        DefaultListModel<String> authorListModel = new DefaultListModel<>();
        bookListModel = new DefaultListModel<>();

        // Criar a JList que é a representacao visual dos autores
        authorList = new JList<>(authorListModel);
        authorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane authorScrollPane = new JScrollPane(authorList);
        panel.add(authorScrollPane, BorderLayout.WEST);

        // // Criar a JList que é a representacao visual dos livros
        bookList = new JList<>(bookListModel);
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane bookScrollPane = new JScrollPane(bookList);
        panel.add(bookScrollPane, BorderLayout.CENTER);  // Add the book scroll pane to the center

        // Adicionar autores a lista
        for (String author : user.listAuthors()) {
            authorListModel.addElement(author);
        }

        // setup dos botoes de acoes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        if (user instanceof Student) {
            //botoes para os estudantes
            JButton requestBtn = new JButton("Requisitar");
            JButton returnBtn = new JButton("Devolver");
            returnBtn.setEnabled(false);
            buttonPanel.add(requestBtn);
            buttonPanel.add(returnBtn);

            requestBtn.addActionListener(event -> {
                requestBook();
            });

            returnBtn.addActionListener(event -> {
                returnBook();
            });

            // Enable/Disable do botao de requisitar livros sempre que a selecao de livro muda
            bookList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    Book selectedBook = bookList.getSelectedValue();

                    if (selectedBook != null) {
                        returnBtn.setEnabled(!selectedBook.isAvailable());
                    }
                }
            });

            // Disable do botao de requisitar livros sempre que a selecao de autor muda
            authorList.addListSelectionListener(e -> {
                returnBtn.setEnabled(false);
            });

        } else if (user instanceof Teacher) {
            //botoes para os professores
            JButton addBtn = new JButton("Adicionar");
            JButton removeBtn = new JButton("Remover");
            buttonPanel.add(addBtn);
            buttonPanel.add(removeBtn);

            addBtn.addActionListener(event -> {
                addBook();
            });

            removeBtn.addActionListener(event -> {
                removeBook();
            });
        }

        // quando é selecionado um autor, atualiza a lista dos seus livros
        authorList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshBookList();

            }
        });

        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void returnBook() {
        Book selectedBook = bookList.getSelectedValue();

        if (selectedBook != null) {
            try {
                ((Student) user).returnBook(selectedBook);
                JOptionPane.showMessageDialog(frame, "Livro devolvido com sucesso!");
                refreshBookList();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erro a devolver livro.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void requestBook() {
        Book selectedBook = bookList.getSelectedValue();
        if (selectedBook != null) {
            try {
                boolean requested = ((Student) user).requestBook(selectedBook);
                if (requested) {

                    JOptionPane.showMessageDialog(frame, "Livro requisitado.");
                } else {

                    JOptionPane.showMessageDialog(frame, "Erro ao requisitar livro.");
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro a requisitar livro: " + e.getMessage());
            }

            refreshLists();
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione um livro para requisitar.");
        }
    }

    private void removeBook() {
        String selectedAuthor = authorList.getSelectedValue();
        Book selectedBook = bookList.getSelectedValue();
        if (selectedAuthor != null && selectedBook != null) {
            try {
                ((Teacher) user).removeBook(selectedAuthor, selectedBook.getTitle());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro a eliminar livro: " + e.getMessage());
            }

            refreshLists();
        } else {
            JOptionPane.showMessageDialog(frame, "Selecione um livro para apagar.");
        }
    }

    private void addBook() {
        JTextField authorField = new JTextField(20);
        JTextField titleField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("Título:"));
        panel.add(titleField);

        int option = JOptionPane.showConfirmDialog(frame, panel,
                "Insira informação do livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If user clicked OK
        if (option == JOptionPane.OK_OPTION) {
            String author = authorField.getText().trim();
            String title = titleField.getText().trim();

            if (!author.isEmpty() && !title.isEmpty()) {
                try {
                    ((Teacher) user).addBook(author, title);

                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Erro a gravar livros: " + e.getMessage());
                }

                refreshLists();
            } else {
                JOptionPane.showMessageDialog(frame, "Autor e título obrigatórios!");
            }
        }
    }

    private void refreshLists() {
        String selectedAuthor = authorList.getSelectedValue();
        DefaultListModel<String> authorModel = (DefaultListModel<String>) authorList.getModel();
        authorModel.clear();

        for (String author : user.listAuthors()) {
            authorModel.addElement(author);
        }

        if (selectedAuthor != null) {
            int index = authorModel.indexOf(selectedAuthor);
            if (index != -1) {
                authorList.setSelectedIndex(index);
            }
        }

        refreshBookList();
    }

    private void refreshBookList() {
        bookListModel.clear();

        int selectedAuthorIndex = authorList.getSelectedIndex();
        if (selectedAuthorIndex != -1) {
            Book[] books = user.getBooksByAuthor(selectedAuthorIndex);
            for (Book book : books) {
                book.setAuthor(authorList.getSelectedValue());
                bookListModel.addElement(book);
            }
        }
    }
}
