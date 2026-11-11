
import javax.swing.*;
import java.awt.*;
import java.sql.*;

class FeeService {

    private static final String URL =
        "jdbc:mysql://localhost:3306/college";

    private static final String USER = "root";
    private static final String PWD = "password";

    public String payFee(String regNo, String amountText) {

        if (regNo == null || regNo.trim().isEmpty()) {
            return "Registration number is required";
        }

        double amount;

        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            return "Enter a valid payment amount";
        }

        if (amount <= 0) {
            return "Amount must be greater than zero";
        }

        String sql =
            "INSERT INTO fees (reg_no, amount) VALUES (?, ?)";

        try (Connection con =
                 DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, regNo.trim());
            ps.setDouble(2, amount);

            ps.executeUpdate();

            return "Payment successful!";

        } catch (SQLException e) {
            return "Payment failed: " + e.getMessage();
        }
    }
}

public class FeeWindow extends JFrame {

    private final JTextField regNo = new JTextField(15);
    private final JTextField amount = new JTextField(15);

    private final JButton pay = new JButton("Pay");
    private final JButton clear = new JButton("Clear");
    private final JButton receipt = new JButton("Receipt");

    private final JLabel status = new JLabel("Enter payment details");

    private final FeeService service = new FeeService();

    public FeeWindow() {

        setTitle("Student Fee Payment");
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Registration Number:"));
        add(regNo);

        add(new JLabel("Amount:"));
        add(amount);

        add(pay);
        add(clear);

        add(receipt);
        add(status);

        pay.addActionListener(e -> processPayment());

        clear.addActionListener(e -> clearFields());

        receipt.addActionListener(e ->
            JOptionPane.showMessageDialog(
                this,
                "Receipt generation can be implemented here."
            )
        );

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(
                java.awt.event.WindowEvent e) {

                dispose();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void processPayment() {

        String registration = regNo.getText();
        String paymentAmount = amount.getText();

        pay.setEnabled(false);
        status.setText("Processing payment...");

        SwingWorker<String, Void> worker =
            new SwingWorker<String, Void>() {

            @Override
            protected String doInBackground() {
                return service.payFee(registration, paymentAmount);
            }

            @Override
            protected void done() {
                try {
                    String result = get();

                    status.setText(result);

                    JOptionPane.showMessageDialog(
                        FeeWindow.this, result
                    );

                } catch (Exception e) {
                    status.setText("Payment processing error");
                }

                pay.setEnabled(true);
            }
        };

        worker.execute();
    }

    private void clearFields() {

        regNo.setText("");
        amount.setText("");
        status.setText("Fields cleared");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() ->
            new FeeWindow().setVisible(true)
        );
    }
}
