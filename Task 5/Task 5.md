Task 5.1 – Four Implementations (3 Marks)
The currency converter accepts an amount in INR and converts it into USD or EUR using the rates supplied in the assignment:
•	USD rate: 0.012
•	EUR rate: 0.011
The following four implementations use the same conversion logic but different event-handling styles.

(a) Frame itself implements ActionListener
Concept: The main JFrame implements the ActionListener interface and defines the actionPerformed() method.
Create Task05/FrameListenerConverter.java.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameListenerConverter extends JFrame
        implements ActionListener {

    JTextField amount = new JTextField(10);

    JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    public FrameListenerConverter() {

        super("Frame Listener Converter");

        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        convert.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 150);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            double value =
                Double.parseDouble(amount.getText());

            String currency = (String) target.getSelectedItem();

            double rate = currency.equals("USD")
                ? 0.012 : 0.011;

            result.setText(
                String.format("%.2f %s", value * rate, currency)
            );

        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new FrameListenerConverter().setVisible(true)
        );
    }
}
(b) Named Inner Class
Concept: A separate named inner class implements ActionListener. This keeps the event-handling logic separate from the main constructor.
Create Task05/NamedInnerConverter.java.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NamedInnerConverter extends JFrame {

    JTextField amount = new JTextField(10);

    JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    public NamedInnerConverter() {

        super("Named Inner Class Converter");

        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        convert.addActionListener(new ConvertHandler());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 150);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    class ConvertHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                double value =
                    Double.parseDouble(amount.getText());

                String currency =
                    (String) target.getSelectedItem();

                double rate = currency.equals("USD")
                    ? 0.012 : 0.011;

                result.setText(
                    String.format("%.2f %s",
                        value * rate, currency)
                );

            } catch (NumberFormatException ex) {
                result.setText("Invalid amount");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new NamedInnerConverter().setVisible(true)
        );
    }
}
(c) Anonymous Inner Class
Concept: An anonymous inner class implements the listener directly when registering the event handler. No separate named handler class is needed.
Create Task05/AnonymousInnerConverter.java.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AnonymousInnerConverter extends JFrame {

    JTextField amount = new JTextField(10);

    JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    public AnonymousInnerConverter() {

        super("Anonymous Inner Class Converter");

        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        convert.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    double value =
                        Double.parseDouble(amount.getText());

                    String currency =
                        (String) target.getSelectedItem();

                    double rate = currency.equals("USD")
                        ? 0.012 : 0.011;

                    result.setText(
                        String.format("%.2f %s",
                            value * rate, currency)
                    );

                } catch (NumberFormatException ex) {
                    result.setText("Invalid amount");
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 150);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new AnonymousInnerConverter().setVisible(true)
        );
    }
}
(d) Lambda Expression
Concept: A lambda expression provides a shorter way to implement a functional interface such as ActionListener.
Create Task05/LambdaConverter.java.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LambdaConverter extends JFrame {

    JTextField amount = new JTextField(10);

    JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    JButton convert = new JButton("Convert");
    JLabel result = new JLabel(" ");

    public LambdaConverter() {

        super("Lambda Converter");

        setLayout(new FlowLayout());

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convert);
        add(result);

        convert.addActionListener(e -> {

            try {
                double value =
                    Double.parseDouble(amount.getText());

                String currency =
                    (String) target.getSelectedItem();

                double rate = currency.equals("USD")
                    ? 0.012 : 0.011;

                result.setText(
                    String.format("%.2f %s",
                        value * rate, currency)
                );

            } catch (NumberFormatException ex) {
                result.setText("Invalid amount");
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 150);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new LambdaConverter().setVisible(true)
        );
    }
}

Task 5.2 – Change the Requirement (2 Marks)
New requirement:
The converter must now:
1.	Convert when the user clicks the Convert button.
2.	Convert when the user presses Enter in the amount field.
3.	Display the result in a dialog box.
4.	Display "Invalid amount" if the entered value cannot be parsed.

Changes required in each implementation

Implementation	Modification
Frame implements ActionListener	Register this with both the button and amount field. Replace result-label-only output with JOptionPane.
Named inner class	Create one ConvertHandler object and register it with both components.
Anonymous inner class	Store the anonymous listener in an ActionListener variable and register it with both components.
Lambda expression	Store one lambda in an ActionListener variable and register it with both components.

Example of the shared listener approach:
ActionListener handler = e -> convert();

convertButton.addActionListener(handler);
amount.addActionListener(handler);
The same handler now responds to both the button click and Enter key.
Complete the Code – Lambda Version
This is the completed version of the exact Converter program supplied in the assignment.
Create Task05/Converter.java.

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Converter extends JFrame {

    private final JTextField amount = new JTextField(10);

    private final JComboBox<String> target =
        new JComboBox<>(new String[]{"USD", "EUR"});

    private final JLabel result = new JLabel(" ");

    private static double rate(String currency) {
        return currency.equals("USD") ? 0.012 : 0.011;
    }

    public Converter() {

        super("Currency Converter");

        setLayout(new FlowLayout());

        JButton convertButton = new JButton("Convert");

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convertButton);
        add(result);

        // TODO 1: One lambda listener for both triggers
        ActionListener handler = e -> convert();

        convertButton.addActionListener(handler);
        amount.addActionListener(handler);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
    }

    private void convert() {

        // TODO 2: Validate, calculate and show dialog
        try {

            double value =
                Double.parseDouble(amount.getText().trim());

            String currency =
                (String) target.getSelectedItem();

            double converted = value * rate(currency);

            String message =
                String.format("%.2f %s", converted, currency);

            result.setText(message);

            JOptionPane.showMessageDialog(
                this, message,
                "Conversion Result",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {

            result.setText("Invalid amount");

            JOptionPane.showMessageDialog(
                this,
                "Invalid amount",
                "Input Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new Converter().setVisible(true)
        );
    }
}

Expected behaviour:

Input	Currency	Expected result
1000	USD	12.00 USD
1000	EUR	11.00 EUR
500	USD	6.00 USD
abc	USD	Invalid amount
Empty	EUR	Invalid amount

The exchange rates are the fixed values provided in the assignment, not live currency rates.
Task 5.3 – Compare the Styles (2 Marks)
The four implementations perform the same conversion operation, but differ in how their event-handling logic is organized.
Comparison of four event-handling styles
Factor	Frame listener	Named inner class	Anonymous inner class	Lambda
Readability	Moderate	Good	Moderate	Very good
Access to frame fields	Direct	Direct	Direct	Direct
Handler reuse	Good	Very good	Possible	Very good
Code length	Moderate	Longer	Moderate	Short
Ease of modification	Moderate	Very good	Moderate	Good
Best use	Small applications	Complex handlers	One-time handlers	Simple event actions

Explanation:
1.	Frame listener: The JFrame itself handles events. It is simple but mixes GUI and event-handling responsibilities.
2.	Named inner class: The handler is separated into its own class, making it easier to reuse and maintain.
3.	Anonymous inner class: The listener is created directly where it is needed. It is suitable for one-time event handling.
4.	Lambda expression: It provides concise code and is suitable for functional interfaces such as ActionListener.

Code changes when the requirement changes

Implementation	Code changes required
Frame listener	Register the frame with the amount field and modify actionPerformed().
Named inner class	Reuse the same handler object for both components and modify its method.
Anonymous inner class	Store the listener in a variable and register it with both components.
Lambda	Store the lambda in an ActionListener variable and register it with both components.

Alternative Concept and Complexity Analysis
Alternative: Use a method reference (this::convert) or one shared Action object for the Convert button, menu item and keyboard shortcut.
Approach 1 – Method Reference
A method reference directly refers to an existing method.
ActionListener handler = this::convert;

convertButton.addActionListener(handler);
amount.addActionListener(handler);
Advantages:
•	Reduces repetitive lambda code.
•	Reuses an existing conversion method.
•	Easy to register with multiple event sources.
Approach 2 – Shared Swing Action
A shared AbstractAction can be used by multiple GUI components.
Action convertAction = new AbstractAction("Convert") {

    @Override
    public void actionPerformed(ActionEvent e) {
        convert();
    }
};

convertButton.setAction(convertAction);
The same action can also be attached to a menu item:
JMenuItem menuItem = new JMenuItem(convertAction);
A keyboard shortcut can be connected using the same action through Swing's key bindings.

Complexity comparison
Let \(k\) represent the number of event sources that trigger the conversion operation.
Factor	Separate listeners	Shared method reference / Action
Handler objects	Potentially multiple	One shared handler
Space complexity	O(k)	O(1) shared handler
Event dispatch	O(1)	O(1)
Adding a new trigger	Register another listener	Reuse the existing handler
Maintenance	May duplicate logic	Centralized logic

Conclusion:
A shared method reference or Swing Action reduces duplicated event-handling code. It is especially useful when the same operation must be triggered by a button, menu item and keyboard shortcut. It also improves consistency because all event sources invoke the same conversion logic.
