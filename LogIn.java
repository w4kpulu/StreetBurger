import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class LogIn extends JFrame{
    private final Font mainFont = new Font("Arial", Font.BOLD, 16);
    private final Font titleFont = new Font("Verdana", Font.BOLD, 24);

    private JTextField emailInput;
    

    public void LogInitial(){
        // Title
        JLabel lblLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lblLoginForm.setFont(titleFont);
        lblLoginForm.setForeground(new Color(34, 45, 65));

        // Email Label and Input
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(mainFont);

        emailInput = new JTextField();
        emailInput.setFont(mainFont);
        emailInput.setPreferredSize(new Dimension(200, 30));

        // Password Label and Input
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(mainFont);

        JPasswordField passwordInput = new JPasswordField();
        passwordInput.setFont(mainFont);
        passwordInput.setPreferredSize(new Dimension(200, 30));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblLoginForm, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(emailInput, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(passwordInput, gbc);

        // Login Button
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.setBackground(new Color(34, 167, 240));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String email = emailInput.getText();
                String password = new String(passwordInput.getPassword());
        
                BufferedReader in = null;
                boolean isAuthenticated = false;
        
                try {
                    in = new BufferedReader(new FileReader("userInfo.txt"));
                    String inData = null;
        
                    while ((inData = in.readLine()) != null) {
                        // Skip empty lines
                        if (inData.trim().isEmpty()) {
                            continue;
                        }
        
                        StringTokenizer st = new StringTokenizer(inData, ",");
        
                        // Check if the line has the expected number of tokens
                        if (st.countTokens() < 5) {
                            System.out.println("Skipping invalid line: " + inData);
                            continue; // Skip this line if it doesn't have enough tokens
                        }
        
                        String custname = st.nextToken();
                        String custemail = st.nextToken();
                        String custphone = st.nextToken();
                        String custaddress = st.nextToken();
                        String custpass = st.nextToken();
        
                        if (email.equals(custemail) && password.equals(custpass)) {
                            JOptionPane.showMessageDialog(LogIn.this,
                                    "Welcome, " + custname + "!",
                                    "Login Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                            isAuthenticated = true;
                            launchMenuGUI();
                        }
                    }
        
                    if (!isAuthenticated) {
                        JOptionPane.showMessageDialog(LogIn.this,
                                "Invalid email or password.",
                                "Login Failed!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    in.close();
                } catch (FileNotFoundException fe) {
                    JOptionPane.showMessageDialog(LogIn.this,
                            "User data file not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IOException iox) {
                    JOptionPane.showMessageDialog(LogIn.this,
                            "Error reading user data file.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        // Sign Up Button
        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setFont(mainFont);
        btnSignUp.setBackground(new Color(34, 167, 240));
        btnSignUp.setForeground(Color.WHITE);
        btnSignUp.setFocusPainted(false);
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    SignUp signUpForm = new SignUp();
                    signUpForm.signIntial();
                    dispose(); // Close the login form
                });
            }
        });

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignUp);

        // Main Frame Layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Login Form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setMinimumSize(new Dimension(350, 450));
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private String CurrentUser(){
        String CurrentUserEmail = emailInput.getText();
        return CurrentUserEmail;
    }


    private void launchMenuGUI() {
        // Hide the login form
        setVisible(false);
        dispose();

        String currentEmail = CurrentUser();

        // Launch the MenuGUI
        SwingUtilities.invokeLater(() -> {
            menuGui menuGUI = new menuGui();
            menuGUI.setCurrentEmail(currentEmail);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LogIn li = new LogIn();
            li.LogInitial();
        });
    }
}
