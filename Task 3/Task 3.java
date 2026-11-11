
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarySearch extends JFrame {

    private final JTextField searchField = new JTextField(20);

    private final JComboBox<String> category =
        new JComboBox<>(new String[]{
            "All", "Programming", "Science", "Mathematics"
        });

    private final JButton searchButton = new JButton("Search");
    private final JButton prevButton = new JButton("Prev");
    private final JButton nextButton = new JButton("Next");
    private final JButton reserveButton = new JButton("Reserve");

    private final JLabel status =
        new JLabel("Enter a book title and click Search.");

    private final DefaultTableModel model =
        new DefaultTableModel(
            new String[]{"Book ID", "Title", "Category", "Author"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    private final JTable table = new JTable(model);

    private final List<String[]> books = new ArrayList<>();

    private int currentPage = 0;
    private static final int PAGE_SIZE = 5;

    public LibrarySearch() {

        super("Campus Library Search");

        setLayout(new BorderLayout(8, 8));

        add(createHeader(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);

        loadBooks();

        searchButton.addActionListener(e -> searchBooks());

        searchField.addActionListener(e -> searchBooks());

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage();
            }
        });

        nextButton.addActionListener(e -> {
            int totalPages =
                (int) Math.ceil((double) getFilteredBooks().size()
                                / PAGE_SIZE);

            if (currentPage + 1 < totalPages) {
                currentPage++;
                displayPage();
            }
        });

        reserveButton.addActionListener(e -> reserveBook());

        displayPage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 500);
        setMinimumSize(new Dimension(500, 350));
        setLocationRelativeTo(null);
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(5, 8));

        JLabel title = new JLabel(
            "Campus Library Search",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel searchPanel = new JPanel(new FlowLayout());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(category);
        searchPanel.add(searchButton);

        header.add(title, BorderLayout.NORTH);
        header.add(searchPanel, BorderLayout.SOUTH);

        return header;
    }

    private JPanel createFooter() {

        JPanel footer = new JPanel(new BorderLayout());

        JPanel navigation = new JPanel(new FlowLayout());

        navigation.add(prevButton);
        navigation.add(nextButton);
        navigation.add(reserveButton);

        footer.add(navigation, BorderLayout.NORTH);
        footer.add(status, BorderLayout.SOUTH);

        return footer;
    }

    private void loadBooks() {

        books.add(new String[]{
            "B101", "Java Programming",
            "Programming", "James Gosling"
        });

        books.add(new String[]{
            "B102", "Python Basics",
            "Programming", "Guido van Rossum"
        });

        books.add(new String[]{
            "B103", "Physics Fundamentals",
            "Science", "Halliday"
        });

        books.add(new String[]{
            "B104", "Advanced Mathematics",
            "Mathematics", "Kreyszig"
        });

        books.add(new String[]{
            "B105", "Data Structures",
            "Programming", "Mark Allen Weiss"
        });

        books.add(new String[]{
            "B106", "Chemistry Basics",
            "Science", "Brown"
        });

        books.add(new String[]{
            "B107", "Discrete Mathematics",
            "Mathematics", "Kenneth Rosen"
        });

        books.add(new String[]{
            "B108", "Object Oriented Java",
            "Programming", "Herbert Schildt"
        });
    }

    private List<String[]> getFilteredBooks() {

        List<String[]> filtered = new ArrayList<>();

        String keyword =
            searchField.getText().trim().toLowerCase();

        String selectedCategory =
            (String) category.getSelectedItem();

        for (String[] book : books) {

            boolean matchesTitle =
                book[1].toLowerCase().contains(keyword);

            boolean matchesCategory =
                selectedCategory.equals("All")
                || book[2].equals(selectedCategory);

            if (matchesTitle && matchesCategory) {
                filtered.add(book);
            }
        }

        return filtered;
    }

    private void searchBooks() {

        currentPage = 0;
        displayPage();
    }

    private void displayPage() {

        model.setRowCount(0);

        List<String[]> filtered = getFilteredBooks();

        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filtered.size());

        for (int i = start; i < end; i++) {
            model.addRow(filtered.get(i));
        }

        int totalPages =
            (int) Math.ceil((double) filtered.size() / PAGE_SIZE);

        status.setText(
            "Books found: " + filtered.size()
            + " | Page: "
            + (filtered.isEmpty() ? 0 : currentPage + 1)
            + " of " + totalPages
        );

        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage + 1 < totalPages);
    }

    private void reserveBook() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a book to reserve."
            );
            return;
        }

        String bookTitle =
            model.getValueAt(selectedRow, 1).toString();

        JOptionPane.showMessageDialog(
            this, "Reservation requested for: " + bookTitle
        );

        status.setText("Reservation requested: " + bookTitle);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new LibrarySearch().setVisible(true)
        );
    }
}
