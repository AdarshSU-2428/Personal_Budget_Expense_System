package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import db.UserService;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    private JButton goToRegisterButton;

    private JTextField regNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JButton registerButton;
    private JButton goToLoginButton;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private final Color PRIMARY = new Color(41, 128, 185);
    private final Color PRIMARY_DARK = new Color(31, 97, 141);
    private final Color BG_COLOR = new Color(236, 240, 241);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color ACCENT = new Color(46, 204, 113);
    private final Color ACCENT_DARK = new Color(39, 174, 96);

    public LoginFrame() {
        setTitle("Personal Expense Manager - Login");
        setSize(480, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG_COLOR);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(BG_COLOR);

        cardPanel.add(createLoginPanel(), "LOGIN");
        cardPanel.add(createRegisterPanel(), "REGISTER");

        add(cardPanel);
        cardLayout.show(cardPanel, "LOGIN");

        setupActions();
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 120));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(25, 0, 20, 0));

        JLabel titleLabel = new JLabel("Expense Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(174, 214, 241));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 219), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));
        formPanel.setMaximumSize(new Dimension(380, 280));

        JLabel emailLabel = createFieldLabel("Email Address");
        loginEmailField = createStyledTextField();

        JLabel passLabel = createFieldLabel("Password");
        loginPasswordField = createStyledPasswordField();

        loginButton = createStyledButton("Sign In", PRIMARY, PRIMARY_DARK);
        goToRegisterButton = createLinkButton("Don't have an account? Register here");

        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginEmailField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginPasswordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(goToRegisterButton);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(formPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ACCENT);
        headerPanel.setPreferredSize(new Dimension(0, 120));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(25, 0, 20, 0));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Fill in your details to register");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(171, 235, 198));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setBackground(WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 214, 219), 1),
                new EmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setMaximumSize(new Dimension(380, 460));

        JLabel nameLabel = createFieldLabel("Full Name");
        regNameField = createStyledTextField();

        JLabel emailLabel = createFieldLabel("Email Address");
        regEmailField = createStyledTextField();

        JLabel passLabel = createFieldLabel("Password");
        regPasswordField = createStyledPasswordField();

        JLabel confirmLabel = createFieldLabel("Confirm Password");
        regConfirmPasswordField = createStyledPasswordField();

        registerButton = createStyledButton("Create Account", ACCENT, ACCENT_DARK);
        goToLoginButton = createLinkButton("Already have an account? Sign in");

        formPanel.add(nameLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(regNameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(regEmailField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(regPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmLabel);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(regConfirmPasswordField);
        formPanel.add(Box.createVerticalStrut(25));
        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(goToLoginButton);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(formPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerWrapper, BorderLayout.CENTER);

        return mainPanel;
    }

    private void setupActions() {
        goToRegisterButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "REGISTER");
            setTitle("Personal Expense Manager - Register");
        });

        goToLoginButton.addActionListener(e -> {
            cardLayout.show(cardPanel, "LOGIN");
            setTitle("Personal Expense Manager - Login");
        });

        loginButton.addActionListener(e -> {
    String email = loginEmailField.getText().trim();
    String password = new String(loginPasswordField.getPassword()).trim();

    if (email.isEmpty() || password.isEmpty()) {
        showError("Please fill in all fields.");
        return;
    }

    int userId = UserService.loginUser(email, password);

    if (userId != -1) {

        String username = UserService.getUsernameById(userId);

        JOptionPane.showMessageDialog(this,
                "Login successful! Welcome, " + username,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new DashboardFrame(userId, username);

    } else {
        showError("Invalid email or password.");
    }
});

        registerButton.addActionListener(e -> {
    String name = regNameField.getText().trim();
    String email = regEmailField.getText().trim();
    String pass = new String(regPasswordField.getPassword()).trim();
    String confirm = new String(regConfirmPasswordField.getPassword()).trim();

    if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
        showError("Please fill in all fields.");
        return;
    }

    if (!email.contains("@")) {
        showError("Please enter a valid email address.");
        return;
    }

    if (pass.length() < 6) {
        showError("Password must be at least 6 characters.");
        return;
    }

    if (!pass.equals(confirm)) {
        showError("Passwords do not match.");
        return;
    }

    boolean success = UserService.registerUser(name, email, pass);

    if (success) {
        JOptionPane.showMessageDialog(this,
                "Registration successful! Please sign in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(cardPanel, "LOGIN");

    } else {
        showError("Registration failed (Email/Username may already exist)");
    }
});
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(320, 38));
        field.setPreferredSize(new Dimension(320, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(320, 38));
        field.setPreferredSize(new Dimension(320, 38));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createStyledButton(String text, Color bg, Color hoverBg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(WHITE);
        button.setBackground(bg);
        button.setMaximumSize(new Dimension(320, 42));
        button.setPreferredSize(new Dimension(320, 42));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hoverBg); }
            public void mouseExited(MouseEvent e) { button.setBackground(bg); }
        });

        return button;
    }

    private JButton createLinkButton(String text) {
        JButton button = new JButton("<html><u>" + text + "</u></html>");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(PRIMARY);
        button.setBackground(null);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return button;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}