Task 1.1 – Diagnose the Design (2 Marks)
The given student fee-payment program has several design deficiencies.

No.	Design deficiency	Explanation and effect
1	Poor separation of interface and logic	The button listener handles GUI operations, database access and payment processing together. This makes the program difficult to maintain and test.
2	Incorrect String comparison	The expression e.getActionCommand() == “Pay” compares object references instead of String contents. The Pay operation may not execute correctly. Use .equals() instead.
3	No input validation	Empty registration numbers, invalid amounts and negative payment values are not checked. This can cause exceptions or incorrect payment records.
4	Database operation on the event-dispatch thread	Database operations can take time. Performing them on the GUI event thread makes the window unresponsive while payment is processed.
5	Use of Thread.sleep()	The three-second delay blocks the GUI thread. Users may experience a frozen window and cannot interact with it.
6	Poor user feedback	The program only prints “Paid” in the console. It does not display a proper confirmation or error message in the window.
7	Receipt button is not implemented	The Receipt button has no separate functionality. Pressing it may execute the Clear operation instead.
8	SQL injection vulnerability	User input is directly concatenated into the SQL statement. Malicious input could modify or damage database operations.
9	Database connection is not closed	The connection and statement are not properly released, which can waste database resources.
10	Window closing is not handled	There is no proper window-closing listener or close operation.
11	Exceptions are not handled	Database operations and Thread.sleep() can throw checked exceptions. Without proper handling, the application may fail.


Task 1.2 – Repair the Design (3 Marks)
Proposed design: Separate the GUI and payment-processing logic using two classes.
Class structure
FeeWindow
Presentation layer
•	Collects registration number
•	Collects payment amount
•	Handles Pay, Clear and Receipt actions
•	Displays success and error messages
Calls payment service
FeeService
Business logic layer
•	Validates input
•	Processes payment
•	Stores payment information
•	Returns payment result
JDBC connection
Database
Data storage layer
•	Stores student fee details
•	Maintains payment records
Task 1.3 – Design Defence (1 Mark)
The redesigned program is easier to test and maintain because the user interface and payment logic are separated into different classes. The FeeWindow class handles user interaction, while the FeeService class performs validation and database operations. Each class can be tested independently. Changes to the payment process do not require major modifications to the interface. Background processing also improves responsiveness. This modular structure reduces code duplication, improves readability and makes future enhancements easier.
Alternative Concept and Complexity Analysis
Alternative: Instead of using one listener with an if-else chain, each button can have its own listener object or a shared Swing Action.
For example:
pay.addActionListener(e -> processPayment());

clear.addActionListener(e -> clearFields());

receipt.addActionListener(e -> generateReceipt());

Comparison	Single listener with if-else	Separate listeners
Event dispatch	Checks action commands	Directly invokes the registered handler
Time complexity	Up to O(k) comparisons	O(1) handler dispatch
Space complexity	O(1) listener objects	O(k) listener objects
Maintainability	Becomes difficult as buttons increase	Easier to maintain individual actions
Code organization	Centralized logic	Separate action logic
Here, k represents the number of buttons.
Conclusion: Separate listeners improve modularity and avoid repeatedly checking action commands. Although they require additional listener objects, the memory overhead is generally small and the design is more suitable for larger GUI applications.
