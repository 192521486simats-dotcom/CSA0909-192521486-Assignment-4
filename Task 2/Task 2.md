Task 2.1 – Model the Events (2 Marks)
An event occurs when the user interacts with a GUI component. The event source generates an event object, which is handled by a registered listener.

Event source	Event object	Listener interface	Action performed
Department JComboBox	ActionEvent	ActionListener	Refresh the doctor list
Doctor JComboBox	ActionEvent	ActionListener	Update selected doctor
Time-slot JComboBox	ActionEvent	ActionListener	Update selected time slot
Book Appointment button	ActionEvent	ActionListener	Validate details and book appointment
Patient ID JTextField	ActionEvent	ActionListener	Optionally process Enter key
Window	WindowEvent	WindowListener / WindowAdapter	Ask for confirmation before closing
Confirmation dialog	User selection	ActionListener (if custom dialog)	Continue booking or cancel closing

Explanation:
•	ActionEvent is generated when a button is clicked or a selection is changed in a combo box.
•	ActionListener handles actions such as selecting a department and booking an appointment.
•	WindowEvent is generated when the user attempts to close the application.
•	WindowAdapter allows us to override only the required window event methods.

Task 2.2 – Design the Component Hierarchy (2 Marks)
The application can be designed using a JFrame as the main window and multiple JPanel containers to organize the components.
Component hierarchy diagram
JFrame – Appointment Desk
Main application window
JPanel – Patient
GridLayout
•	JLabel
•	JTextField
JPanel – Appointment
GridLayout
•	JLabel
•	JComboBox Department
•	JComboBox Doctor
•	JComboBox Time Slot
JPanel – Actions
FlowLayout
•	JButton Book
•	JButton Clear
JPanel – Status
FlowLayout
- JLabel Status
The panels are arranged inside the JFrame using BorderLayout.

Components and their purposes

Component	Java class	Purpose
Main window	JFrame	Displays the appointment application
Container	JPanel	Groups related components
Patient ID	JTextField	Accepts patient ID
Department	JComboBox	Displays available departments
Doctor	JComboBox	Displays doctors in the selected department
Time slot	JComboBox	Displays available appointment times
Book button	JButton	Submits appointment details
Status	JLabel	Displays success or error messages
Confirmation	JOptionPane	Confirms booking or closing

Task 2.3 – Design the Handler Logic (3 Marks)
The handler logic contains two important operations:
(a) Refreshing the doctor list when the department changes
Logic:
1.	Read the selected department.
2.	Find the doctors belonging to that department.
3.	Remove the previous doctor entries.
4.	Add the new doctors to the doctor combo box.
5.	Select the first available doctor if the list is not empty.
6.	Disable doctor selection if no doctors are available.

Pseudocode:
ALGORITHM RefreshDoctorList

BEGIN
    department ← selected department

    doctorList.removeAllItems()

    IF department is not selected THEN
        RETURN
    END IF

    doctors ← doctorMap.get(department)

    IF doctors is not empty THEN
        FOR EACH doctor IN doctors DO
            doctorList.addItem(doctor)
        END FOR

        doctorList.setEnabled(TRUE)
    ELSE
        doctorList.setEnabled(FALSE)
    END IF

END
(b) Validating and booking the appointment
Logic:
1.	Read the patient ID.
2.	Check whether the patient ID is empty.
3.	Check whether a department is selected.
4.	Check whether a doctor is selected.
5.	Check whether a time slot is selected.
6.	If all values are valid, send the appointment request.
7.	Display confirmation or an error message.
8.	Update the booking-in-progress status.

Pseudocode:
ALGORITHM BookAppointment

BEGIN
    bookingInProgress ← TRUE

    patientID ← patientField.getText()
    department ← departmentList.getSelectedItem()
    doctor ← doctorList.getSelectedItem()
    timeSlot ← timeList.getSelectedItem()

    IF patientID is empty THEN
        DISPLAY "Enter Patient ID"
        bookingInProgress ← FALSE
        RETURN
    END IF

    IF department is not selected OR
       doctor is not selected OR
       timeSlot is not selected THEN

        DISPLAY "Complete all appointment details"
        bookingInProgress ← FALSE
        RETURN
    END IF

    result ← appointmentService.book(
        patientID, department, doctor, timeSlot
    )

    IF result is successful THEN
        DISPLAY "Appointment Booked Successfully"
    ELSE
        DISPLAY "Appointment Booking Failed"
    END IF

    bookingInProgress ← FALSE

END

Task 2.4 – Trace the Event Sequence (1 Mark)
The following sequence explains how the application behaves from opening to closing.
1.	Application starts
The main() method is executed and schedules GUI creation using SwingUtilities.invokeLater().
2.	Window initialization
The constructor creates the JFrame, panels, labels, text fields, combo boxes and buttons.
3.	Window becomes visible
setVisible(true) displays the appointment window.
4.	Department selection
An ActionEvent occurs and the department listener refreshes the doctor list.
5.	Appointment booking
The receptionist enters the patient details, selects the doctor and time slot, and clicks Book. The booking handler validates the information and processes the request.
6.	Booking result
A confirmation or error message is displayed, and the booking-in-progress status is updated.
7.	Window closing
windowClosing(WindowEvent e) is invoked. If a booking is in progress, the application asks for confirmation. If closing is permitted, it disposes of the window.


Window event methods:

Method	Purpose
windowOpened()	Called when the window is first opened
windowClosing()	Called when the user attempts to close the window
windowClosed()	Called after the window has been disposed
windowActivated()	Called when the window becomes active
windowDeactivated()	Called when the window loses activation

Comparison with Applet Life Cycle

GUI window	Applet life-cycle method
Constructor	Initializes the window
setVisible(true)	Makes the interface visible
windowOpened()	No exact equivalent
User interaction	Event-driven operations
Window closing and disposal	stop() and destroy() represent applet shutdown stages

An applet uses predefined life-cycle methods such as init(), start(), paint(), stop() and destroy(). A desktop Swing window instead uses constructors, visibility methods and window events.
Alternative Concept and Complexity Analysis
Alternative: Fetch the doctor list from the database every time the department changes instead of loading all doctors once into a Map<String, List<Doctor>>.
Approach 1 – Preloaded Map
•	Load doctor records from the database when the application starts.
•	Store departments as keys and their doctor lists as values.
•	When a department changes, retrieve its doctor list directly from the map.
Map<String, List<Doctor>> doctorMap = new HashMap<>();

List<Doctor> doctors =
    doctorMap.get(selectedDepartment);

Approach 2 – Database Fetch on Every Change
•	When the department changes, execute a database query.
•	Retrieve doctors belonging to the selected department.
•	Update the doctor combo box with the returned records.
String sql =
    "SELECT * FROM doctors WHERE department = ?";

PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, selectedDepartment);

ResultSet rs = ps.executeQuery();

Complexity comparison
Let:
•	d = number of departments.
•	m = total number of doctor records across all departments.
•	m_i = number of doctors in the selected department.

Factor	Preloaded Map	Database fetch
Initial loading	O(m+d)	No full preload required
Department change	O(1+m_i) average	Database query +  O(m_i) result processing
Memory usage	O(m+d)	O(m_i) for currently loaded results, excluding database storage
Database access	Mainly during initialization	Every department change
Responsiveness	Fast local retrieval	Depends on database response time

If there are m doctors in each of d departments, the total doctor records may be  dm . In that case, preloaded-map memory becomes  O (dm+d).
Conclusion:
A preloaded Map<String, List<Doctor>> is suitable when doctor information does not change frequently and quick department switching is required. Fetching from the database each time reduces the amount of data held in application memory, but introduces database latency and repeated queries. The appropriate approach depends on the size of the doctor database and how frequently its records change.
