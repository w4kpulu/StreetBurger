import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class SignUp extends JFrame {
    private final Font mainFont = new Font("Arial", Font.BOLD, 16);
    private final Font titleFont = new Font("Verdana", Font.BOLD, 24);

    // Get input from user and store it to userInformation.txt
    public void signIntial() {

        // Title
        JLabel signlbl = new JLabel("Sign Up Form", JLabel.CENTER);
        signlbl.setFont(titleFont);
        signlbl.setForeground(new Color(34, 45, 65));

        // Customer Name label and input
        JLabel lblname = new JLabel("Name");
        lblname.setFont(mainFont);

        JTextField custName = new JTextField();
        custName.setFont(mainFont);
        custName.setPreferredSize(new Dimension(200, 30)); // Text field size

        // Customer email label and input
        JLabel lblemail = new JLabel("Email");
        lblemail.setFont(mainFont);

        JTextField custEmail = new JTextField();
        custEmail.setFont(mainFont);
        custEmail.setPreferredSize(new Dimension(200, 30)); // Text field size

        // Phone number label and input
        JLabel lblphoneNum = new JLabel("Phone Number");
        lblphoneNum.setFont(mainFont);

        JTextField custPhoneNum = new JTextField();
        custPhoneNum.setFont(mainFont);
        custPhoneNum.setPreferredSize(new Dimension(200, 30)); // Text field size

        // Address label and input
        JLabel lbladdress = new JLabel("Address");
        lbladdress.setFont(mainFont);

        JTextField custAddr = new JTextField();
        custAddr.setFont(mainFont);
        custAddr.setPreferredSize(new Dimension(200, 30)); // Text field size

        // Password label and input (Use JPasswordField)
        JLabel lblpass = new JLabel("Password");
        lblpass.setFont(mainFont);

        JPasswordField custPass = new JPasswordField();
        custPass.setFont(mainFont);
        custPass.setPreferredSize(new Dimension(200, 30)); // Text field size

        // Form Panel
        JPanel signPanel = new JPanel();
        signPanel.setLayout(new GridBagLayout());
        signPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints used to resize the component to fit in the display area
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        signPanel.add(signlbl, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        signPanel.add(lblname, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        signPanel.add(custName, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        signPanel.add(lblemail, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        signPanel.add(custEmail, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        signPanel.add(lblphoneNum, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        signPanel.add(custPhoneNum, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        signPanel.add(lbladdress, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        signPanel.add(custAddr, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        signPanel.add(lblpass, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        signPanel.add(custPass, gbc);

        // Sign Up button
        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(mainFont);
        btnSignUp.setBackground(new Color(34, 167, 240));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFocusPainted(false);
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String name = custName.getText();
                String email = custEmail.getText();
                String phoneNo = custPhoneNum.getText();
                String addr = custAddr.getText();
                String pass = new String(custPass.getPassword());

                // Validate if all fields are filled
                if (name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || addr.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUp.this,
                            "All fields are required!",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("userInfo.txt", true))) {
                    writer.write(name + "," + email + "," + phoneNo + "," + addr + "," + pass);
                    writer.newLine();
                    JOptionPane.showMessageDialog(SignUp.this,
                            "User information saved successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SignUp.this,
                            "Error saving user information: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Back to Login Button
        JButton btnBackToLogin = new JButton("Log In");
        btnBackToLogin.setFont(mainFont);
        btnBackToLogin.setBackground(new Color(34, 167, 240));
        btnBackToLogin.setForeground(Color.WHITE);
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    LogIn li = new LogIn();
                    li.LogInitial();
                    dispose();
                });
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Center align buttons
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnBackToLogin);

        // Main Frame Layout
        setLayout(new BorderLayout());
        add(signPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Sign Up Form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 600);
        setMinimumSize(new Dimension(350, 500));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignUp su = new SignUp();
            su.signIntial();
        });
    }
}
