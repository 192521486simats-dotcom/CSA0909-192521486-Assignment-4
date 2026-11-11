Task 6.1 – Interpret the Problem (2 Marks)
1. Identify the users
User	Responsibilities
Student	Enter details and register for campus events.
Cultural Committee Member	View, filter and export registration records.
2. Identify the screens
Screen 1 – Event Registration Screen
•	Student name.
•	Student registration number.
•	Department.
•	Event selection.
•	Register button.
•	Clear button.
•	Registration status message.
Screen 2 – Registration Management Screen
•	Display all registrations in a JTable.
•	Filter registrations by event.
•	Export the filtered registration list.
•	Refresh registration records.



3. Identify the data

Field	Description
Registration ID	Unique registration identifier
Student ID	Student registration number
Student Name	Name of the student
Department	Student's department
Event ID	Selected event identifier
Event Name	Name of the event
Registration Date	Date and time of registration

4. Identify business rules
1.	Student name, student ID, department and event must not be empty.
2.	A student can register only once for the same event.
3.	A student can register for different events.
4.	Invalid inputs must be rejected with clear messages.
5.	Database operations must not freeze the GUI.
6.	Registration records must be stored in MySQL.

5. Testable requirements
ID	Requirement	Expected result
FR01	Register a student	Registration is stored in MySQL.
FR02	Validate input	Empty or invalid fields are rejected.
FR03	Prevent duplicates	Duplicate student-event registration is rejected.
FR04	View registrations	Records appear in JTable.
FR05	Filter by event	Only matching registrations are displayed.
FR06	Export registrations	Filtered records are exported to a CSV file.
NFR01	GUI responsiveness	Database operations do not block the GUI.

Task 6.2 – Screen and Event Flow (2 Marks)
Screen 1 – Event Registration
JAVA CRUD Operations with MySQL for beginners | by Uvindu Dharmawardana | Medium
uvindu-52078.medium.com
Components:
•	JLabel – Field descriptions.
•	JTextField – Student name and registration number.
•	JComboBox – Department and event selection.
•	JButton – Register and Clear.
•	JLabel – Status messages.
Screen 2 – Registration Management
RTS - Event Registration Portal
thehypesolutions.com
Components:
•	JTable – Displays student registration details.
•	JComboBox – Selects an event filter.
•	JButton – Filter, Refresh and Export.
•	JMenuBar – Provides navigation and application actions.
•	JScrollPane – Makes the registration table scrollable.
Menu structure
Campus Event Registration
│
├── File
│   ├── Export Registrations
│   └── Exit
│
├── Registration
│   ├── New Registration
│   └── View Registrations
│
└── Help
    └── About
Task 6.3 – Critical Design Decisions (2 Marks)
1. Input validation
Validate the student name, registration number, department and event before sending data to the database.
Example:
if (studentName.isEmpty()
        || studentId.isEmpty()
        || department.isEmpty()
        || event.isEmpty()) {

    JOptionPane.showMessageDialog(
        frame,
        "Please fill all required fields."
    );

    return;
}
2. Duplicate registration checking
Use a database-level unique constraint on the combination of student ID and event ID.
UNIQUE (student_id, event_id)
This ensures that the same student cannot register for the same event more than once, including when two registration requests happen at nearly the same time.
3. Database access
Use JDBC with PreparedStatement instead of building SQL queries through string concatenation.
Advantages:
•	Helps prevent SQL injection.
•	Supports parameterized queries.
•	Separates SQL statements from user input.
4. Background database operations
Use SwingWorker for database operations that may take time.
new SwingWorker<Void, Void>() {

    @Override
    protected Void doInBackground() throws Exception {
        // Perform database operation
        return null;
    }

    @Override
    protected void done() {
        // Update the GUI
    }

}.execute();
Reason: Swing GUI updates should happen on the Event Dispatch Thread (EDT), while long database operations should run in the background to keep the application responsive.
Task 6.4 – Classes, Handlers and Pseudocode (4 Marks)
1.	Proposed class diagram
 
2. Responsibility of each class
Class	Responsibility
CampusEventApp	Builds Swing screens, handles buttons, displays results.
RegistrationService	Validates input and applies registration rules.
RegistrationDAO	Executes SQL queries and inserts registration records.
Database	Establishes the MySQL JDBC connection.
3. Register operation – Pseudocode
FUNCTION RegisterStudent

    READ student name
    READ student ID
    READ department
    READ selected event

    IF any required field is empty THEN
        DISPLAY validation error
        RETURN
    END IF

    START background database operation

        CALL RegistrationService

        CHECK whether registration is valid

        INSERT registration into MySQL

        IF duplicate student-event combination THEN
            DISPLAY duplicate registration message
        ELSE
            DISPLAY registration successful
        END IF

    END background operation

END FUNCTION
4. Filter operation – Pseudocode
FUNCTION FilterRegistrations

    READ selected event

    START background database operation

        CONNECT to MySQL

        IF selected event is All Events THEN
            FETCH all registrations
        ELSE
            FETCH registrations for selected event
        END IF

        RETURN matching records

    END background operation

    DISPLAY records in JTable

END FUNCTION
Task 6.5 – Implement and Demonstrate (3 Marks)
The following implementation combines the GUI, service and DAO layers as separate nested classes in one Java file to make submission and execution easier. It includes:
•	Student registration form.
•	Registration management table.
•	Event filtering.
•	CSV export.
•	MySQL database integration.
•	Duplicate registration protection.
•	Background database operations using SwingWorker.
•	Menu navigation.
Step 1 – MySQL database setup
Create Task06/database.sql.

CREATE DATABASE campus_events;

USE campus_events;

CREATE TABLE events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(30) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT unique_student_event
        UNIQUE (student_id, event_id),

    CONSTRAINT fk_event
        FOREIGN KEY (event_id)
        REFERENCES events(event_id)
);

INSERT INTO events (event_name) VALUES
('Technical Symposium'),
('Cultural Fest'),
('Sports Meet');
Step 2 – Complete Java implementation
Create Task06/CampusEventApp.java.
Before running, update the database username and password in the Database class to match your MySQL configuration.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CampusEventApp {

    // ---------------- DATABASE LAYER ----------------

    static class Database {

        private static final String URL =
            "jdbc:mysql://localhost:3306/campus_events";

        private static final String USER = "root";
        private static final String PASSWORD = "your_password";

        static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(
                URL, USER, PASSWORD
            );
        }
    }

    // ---------------- DATA MODEL ----------------

    static class Registration {

        int registrationId;
        String studentId;
        String studentName;
        String department;
        String eventName;
        String registrationDate;

        Registration(int id, String studentId,
                     String studentName, String department,
                     String eventName, String date) {

            this.registrationId = id;
            this.studentId = studentId;
            this.studentName = studentName;
            this.department = department;
            this.eventName = eventName;
            this.registrationDate = date;
        }
    }

    static class EventOption {

        int id;
        String name;

        EventOption(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // ---------------- DAO LAYER ----------------

    static class RegistrationDAO {

        void insertRegistration(
                String studentId,
                String studentName,
                String department,
                int eventId) throws SQLException {

            String sql =
                "INSERT INTO registrations " +
                "(student_id, student_name, department, event_id) " +
                "VALUES (?, ?, ?, ?)";

            try (Connection con = Database.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, studentId);
                ps.setString(2, studentName);
                ps.setString(3, department);
                ps.setInt(4, eventId);

                ps.executeUpdate();
            }
        }

        List<Registration> getRegistrations(
                int eventId) throws SQLException {

            List<Registration> records = new ArrayList<>();

            String sql =
                "SELECT r.registration_id, r.student_id, " +
                "r.student_name, r.department, e.event_name, " +
                "r.registration_date " +
                "FROM registrations r " +
                "JOIN events e ON r.event_id = e.event_id " +
                (eventId == 0 ? "" : "WHERE r.event_id = ? ") +
                "ORDER BY r.registration_id DESC";

            try (Connection con = Database.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                if (eventId != 0) {
                    ps.setInt(1, eventId);
                }

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        records.add(new Registration(
                            rs.getInt("registration_id"),
                            rs.getString("student_id"),
                            rs.getString("student_name"),
                            rs.getString("department"),
                            rs.getString("event_name"),
                            rs.getString("registration_date")
                        ));
                    }
                }
            }

            return records;
        }
    }

    // ---------------- SERVICE LAYER ----------------

    static class RegistrationService {

        private final RegistrationDAO dao =
            new RegistrationDAO();

        void registerStudent(
                String studentId,
                String studentName,
                String department,
                int eventId) throws SQLException {

            if (studentId == null || studentId.trim().isEmpty()
                    || studentName == null
                    || studentName.trim().isEmpty()
                    || department == null
                    || department.trim().isEmpty()
                    || eventId <= 0) {

                throw new IllegalArgumentException(
                    "Please fill all required fields."
                );
            }

            dao.insertRegistration(
                studentId.trim(),
                studentName.trim(),
                department.trim(),
                eventId
            );
        }

        List<Registration> getRegistrations(
                int eventId) throws SQLException {

            return dao.getRegistrations(eventId);
        }
    }

    // ---------------- GUI LAYER ----------------

    private final RegistrationService service =
        new RegistrationService();

    private JFrame frame;

    private JTextField studentNameField;
    private JTextField studentIdField;

    private JComboBox<String> departmentBox;
    private JComboBox<EventOption> registrationEventBox;
    private JComboBox<EventOption> filterEventBox;

    private JTable table;
    private DefaultTableModel tableModel;

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public CampusEventApp() {

        frame = new JFrame("Campus Event Registration");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(
            createRegistrationScreen(), "registration"
        );

        mainPanel.add(
            createManagementScreen(), "management"
        );

        frame.setJMenuBar(createMenuBar());
        frame.add(mainPanel);

        frame.setSize(850, 550);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ---------------- MENU ----------------

    private JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu registrationMenu = new JMenu("Registration");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exportItem =
            new JMenuItem("Export Registrations");

        JMenuItem exitItem = new JMenuItem("Exit");

        JMenuItem newItem =
            new JMenuItem("New Registration");

        JMenuItem viewItem =
            new JMenuItem("View Registrations");

        JMenuItem aboutItem = new JMenuItem("About");

        exportItem.addActionListener(e -> exportCSV());

        exitItem.addActionListener(e ->
            frame.dispose()
        );

        newItem.addActionListener(e ->
            cardLayout.show(mainPanel, "registration")
        );

        viewItem.addActionListener(e -> {
            cardLayout.show(mainPanel, "management");
            loadRegistrations();
        });

        aboutItem.addActionListener(e ->
            JOptionPane.showMessageDialog(
                frame,
                "Campus Event Registration Application"
            )
        );

        fileMenu.add(exportItem);
        fileMenu.add(exitItem);

        registrationMenu.add(newItem);
        registrationMenu.add(viewItem);

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(registrationMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    // ---------------- REGISTRATION SCREEN ----------------

    private JPanel createRegistrationScreen() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));

        JLabel title = new JLabel(
            "Campus Event Registration",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBorder(
            BorderFactory.createEmptyBorder(30, 100, 30, 100)
        );

        studentNameField = new JTextField();
        studentIdField = new JTextField();

        departmentBox = new JComboBox<>(
            new String[]{
                "Information Technology",
                "Computer Science",
                "Electronics",
                "Mechanical",
                "Civil"
            }
        );

        registrationEventBox = new JComboBox<>();

        registrationEventBox.addItem(
            new EventOption(1, "Technical Symposium")
        );

        registrationEventBox.addItem(
            new EventOption(2, "Cultural Fest")
        );

        registrationEventBox.addItem(
            new EventOption(3, "Sports Meet")
        );

        form.add(new JLabel("Student Name:"));
        form.add(studentNameField);

        form.add(new JLabel("Student ID:"));
        form.add(studentIdField);

        form.add(new JLabel("Department:"));
        form.add(departmentBox);

        form.add(new JLabel("Select Event:"));
        form.add(registrationEventBox);

        JButton registerButton = new JButton("Register");
        JButton clearButton = new JButton("Clear");
        JButton viewButton = new JButton("View Registrations");

        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(registerButton);
        buttons.add(clearButton);
        buttons.add(viewButton);

        registerButton.addActionListener(
            e -> registerStudent()
        );

        clearButton.addActionListener(e -> {
            studentNameField.setText("");
            studentIdField.setText("");
        });

        viewButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "management");
            loadRegistrations();
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    // ---------------- MANAGEMENT SCREEN ----------------

    private JPanel createManagementScreen() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel(
            "Registration Management",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 20));

        filterEventBox = new JComboBox<>();

        filterEventBox.addItem(
            new EventOption(0, "All Events")
        );

        filterEventBox.addItem(
            new EventOption(1, "Technical Symposium")
        );

        filterEventBox.addItem(
            new EventOption(2, "Cultural Fest")
        );

        filterEventBox.addItem(
            new EventOption(3, "Sports Meet")
        );

        JButton filterButton = new JButton("Filter");
        JButton refreshButton = new JButton("Refresh");
        JButton exportButton = new JButton("Export CSV");
        JButton backButton = new JButton("New Registration");

        JPanel controls = new JPanel(new FlowLayout());

        controls.add(new JLabel("Event:"));
        controls.add(filterEventBox);
        controls.add(filterButton);
        controls.add(refreshButton);
        controls.add(exportButton);
        controls.add(backButton);

        tableModel = new DefaultTableModel(
            new String[]{
                "ID", "Student ID", "Student Name",
                "Department", "Event", "Date"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        filterButton.addActionListener(e ->
            loadRegistrations()
        );

        refreshButton.addActionListener(e ->
            loadRegistrations()
        );

        exportButton.addActionListener(e ->
            exportCSV()
        );

        backButton.addActionListener(e ->
            cardLayout.show(mainPanel, "registration")
        );

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.NORTH);
        top.add(controls, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ---------------- REGISTER STUDENT ----------------

    private void registerStudent() {

        String studentName = studentNameField.getText();
        String studentId = studentIdField.getText();

        String department =
            (String) departmentBox.getSelectedItem();

        EventOption event =
            (EventOption) registrationEventBox.getSelectedItem();

        if (event == null) {
            JOptionPane.showMessageDialog(
                frame, "Please select an event."
            );
            return;
        }

        // Prevent repeated submissions while saving
        final JButton registerButton = findRegisterButton();
        if (registerButton != null) {
            registerButton.setEnabled(false);
        }

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                service.registerStudent(
                    studentId,
                    studentName,
                    department,
                    event.id
                );

                return null;
            }

            @Override
            protected void done() {

                if (registerButton != null) {
                    registerButton.setEnabled(true);
                }

                try {

                    get();

                    JOptionPane.showMessageDialog(
                        frame,
                        "Registration successful!"
                    );

                    studentNameField.setText("");
                    studentIdField.setText("");

                } catch (Exception ex) {

                    Throwable cause = ex.getCause();

                    if (cause instanceof IllegalArgumentException) {

                        JOptionPane.showMessageDialog(
                            frame,
                            cause.getMessage(),
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE
                        );

                    } else if (cause instanceof SQLException
                            && "23000".equals(
                                ((SQLException) cause).getSQLState()
                            )) {

                        JOptionPane.showMessageDialog(
                            frame,
                            "This student is already registered " +
                            "for this event.",
                            "Duplicate Registration",
                            JOptionPane.WARNING_MESSAGE
                        );

                    } else {

                        JOptionPane.showMessageDialog(
                            frame,
                            "Registration failed: " +
                            (cause == null
                                ? ex.getMessage()
                                : cause.getMessage()),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }

        }.execute();
    }

    private JButton findRegisterButton() {

        return findButton(
            (Container) mainPanel.getComponent(0),
            "Register"
        );
    }

    private JButton findButton(Container container, String text) {

        for (Component component : container.getComponents()) {

            if (component instanceof JButton
                    && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            }

            if (component instanceof Container) {

                JButton found = findButton(
                    (Container) component, text
                );

                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }

    // ---------------- LOAD / FILTER REGISTRATIONS ----------------

    private void loadRegistrations() {

        EventOption selected =
            (EventOption) filterEventBox.getSelectedItem();

        final int eventId =
            selected == null ? 0 : selected.id;

        new SwingWorker<List<Registration>, Void>() {

            @Override
            protected List<Registration> doInBackground()
                    throws Exception {

                return service.getRegistrations(eventId);
            }

            @Override
            protected void done() {

                try {

                    List<Registration> records = get();

                    tableModel.setRowCount(0);

                    for (Registration r : records) {

                        tableModel.addRow(new Object[]{
                            r.registrationId,
                            r.studentId,
                            r.studentName,
                            r.department,
                            r.eventName,
                            r.registrationDate
                        });
                    }

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "Unable to load registrations: " +
                        ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        }.execute();
    }

    // ---------------- EXPORT CSV ----------------

    private void exportCSV() {

        if (tableModel == null || tableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                frame, "There are no records to export."
            );

            return;
        }

        JFileChooser chooser = new JFileChooser();

        chooser.setSelectedFile(
            new File("campus_registrations.csv")
        );

        if (chooser.showSaveDialog(frame)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        // Take a snapshot of the displayed table on the EDT
        int rows = tableModel.getRowCount();
        int columns = tableModel.getColumnCount();

        String[] headers = new String[columns];
        String[][] data = new String[rows][columns];

        for (int c = 0; c < columns; c++) {
            headers[c] = tableModel.getColumnName(c);
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                Object value = tableModel.getValueAt(r, c);
                data[r][c] = value == null ? "" : value.toString();
            }
        }

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {

                try (BufferedWriter writer =
                    Files.newBufferedWriter(
                        file.toPath(), StandardCharsets.UTF_8
                    )) {

                    writeCSVRow(writer, headers);

                    for (String[] row : data) {
                        writeCSVRow(writer, row);
                    }
                }

                return null;
            }

            @Override
            protected void done() {

                try {
                    get();

                    JOptionPane.showMessageDialog(
                        frame,
                        "CSV exported successfully:\n" +
                        file.getAbsolutePath()
                    );

                } catch (Exception ex) {

                    JOptionPane.showMessageDialog(
                        frame,
                        "Export failed: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        }.execute();
    }

    private static void writeCSVRow(
            BufferedWriter writer, String[] values)
            throws IOException {

        for (int i = 0; i < values.length; i++) {

            if (i > 0) {
                writer.write(",");
            }

            String escaped = values[i].replace("\"", "\"\"");

            writer.write("\"" + escaped + "\"");
        }

        writer.newLine();
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CampusEventApp app = new CampusEventApp();

            app.frame.setVisible(true);
        });
    }
}
Step 3 – Configure and run the application
Required software:
•	JDK 8 or later.
•	MySQL Server.
•	MySQL Connector/J JDBC driver.
•	Any Java IDE or terminal.
Execution steps:
1.	Open MySQL Workbench.
2.	Execute database.sql.
3.	Open CampusEventApp.java.
4.	Change the database password in the Database class.
5.	Add the MySQL Connector/J JAR to your project's classpath.
6.	Compile and run.
For Windows terminal, assuming the driver JAR is in the same folder:
javac -cp ".;mysql-connector-j-8.4.0.jar" CampusEventApp.java

java -cp ".;mysql-connector-j-8.4.0.jar" CampusEventApp
Replace the JAR filename with the actual filename of the driver you downloaded.
Step 4 – Demonstration scenarios
Scenario	Input/action	Expected result
Normal registration	Valid student details and Technical Symposium	Registration successful
Invalid registration	Empty student name or student ID	Validation error
Duplicate registration	Register the same student for the same event twice	Duplicate registration warning
Different event	Same student registers for Cultural Fest	Registration successful
View records	Open View Registrations	Records appear in JTable
Filter records	Select Sports Meet	Only Sports Meet records appear
Export records	Click Export CSV	Filtered table records are saved as CSV
Expected application screens
GitHub - afa-farkhod/Registration-Form: Registration Form Windows Desktop Application built on Eclipse Window Builder · GitHub
Registration form
Create List In Java Swing
Registration management
A Survey Management System: Design and Implementation Using Java Swing and MySQL
Success message
GitHub - lucassmaniotto/Javadex: Trabalho Final do 3º Semestre de CC da UFFS Chapecó - CRUD de uma Pokedéx · GitHub
Duplicate warning
These are visual references, not actual screenshots of your executed program. Capture your own application screenshots for submission.
Alternative Concept and Complexity Analysis
Alternative: Load all registration records into a JTable versus retrieving records page by page using SQL LIMIT and OFFSET.
Approach 1 – Load all records
SELECT * FROM registrations;
•	Retrieves every registration record at once.
•	Displays all records in the table.
•	Requires more memory as the number of records increases.
•	Suitable for smaller registration datasets.
Approach 2 – Pagination using LIMIT and OFFSET
SELECT * FROM registrations
ORDER BY registration_id
LIMIT 100 OFFSET 0;
For the next page:
SELECT * FROM registrations
ORDER BY registration_id
LIMIT 100 OFFSET 100;
•	Loads only 100 records at a time.
•	Reduces the amount of data held in the GUI.
•	Makes large registration tables easier to navigate.
•	Can require additional database queries when moving between pages.
Complexity comparison
Let (N) be the total number of registrations and \(P\) be the page size.

Factor	Load all records	Pagination
Initial records retrieved	N	P
Approximate result storage	O(N)	O(P)
Initial data transfer	O(N)	O(P)
Database requests	Usually one	Multiple as pages are requested
GUI memory usage	Higher	Lower
Suitability for 100,000 records	Less suitable	More suitable
Example with 100,000 registrations:
•	Loading all records retrieves 100,000 rows.
•	Pagination with 100 records per page retrieves 100 rows initially.
•	The user can navigate through 1,000 pages.
Note: OFFSET queries can become slower at high page numbers because the database may still need to scan and skip earlier rows. Pagination improves initial loading and memory usage, but does not guarantee constant-time database queries.
Conclusion:
For a small campus event application, loading all records may be sufficient. For a large system with 100,000 or more registrations, pagination using LIMIT and OFFSET is preferable because it reduces initial data transfer and GUI memory usage. Database operations should run in SwingWorker to keep the interface responsive.
