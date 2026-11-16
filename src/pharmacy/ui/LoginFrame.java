
package pharmacy.ui;

import pharmacy.dao.UserDao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    // =====================================
    // BLACK AND GOLD COLOUR THEME
    // =====================================

    private static final Color BLACK =
            new Color(18, 18, 18);

    private static final Color DARK =
            new Color(30, 30, 30);

    private static final Color GOLD =
            new Color(254, 190, 35);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color MUTED =
            new Color(175, 175, 175);

    private static final Color BORDER =
            new Color(75, 75, 75);

    // =====================================
    // LOGIN COMPONENTS
    // =====================================

    private final JTextField username =
            new JTextField(20);

    private final JPasswordField password =
            new JPasswordField(20);

    private final JButton loginButton =
            new JButton("LOGIN");

    public LoginFrame() {

        setTitle("Pharmacy Management System - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(520, 570);

        setMinimumSize(new Dimension(480, 520));

        setLocationRelativeTo(null);

        setResizable(false);

        getContentPane().setBackground(BLACK);

        setLayout(new BorderLayout());

        add(buildLoginPanel(), BorderLayout.CENTER);

        getRootPane().setDefaultButton(loginButton);
    }

    // =====================================
    // MAIN LOGIN PANEL
    // =====================================

    private JPanel buildLoginPanel() {

        JPanel root = new JPanel(new GridBagLayout());

        root.setBackground(BLACK);

        root.setBorder(
                new EmptyBorder(30, 45, 30, 45)
        );

        JPanel card = new JPanel();

        card.setBackground(DARK);

        card.setPreferredSize(
                new Dimension(390, 440)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(28, 30, 28, 30)
                )
        );

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        // Logo / branding

        JLabel logo = new JLabel("MEDICARE");

        logo.setFont(
                new Font("Segoe UI", Font.BOLD, 30)
        );

        logo.setForeground(GOLD);

        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel systemName = new JLabel(
                "PHARMACY MANAGEMENT SYSTEM"
        );

        systemName.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        systemName.setForeground(WHITE);

        systemName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Manage your pharmacy with confidence"
        );

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        subtitle.setForeground(MUTED);

        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(logo);
        card.add(Box.createVerticalStrut(5));
        card.add(systemName);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitle);

        // Gold separator

        JPanel separator = new JPanel();

        separator.setBackground(GOLD);

        separator.setMaximumSize(
                new Dimension(100, 3)
        );

        separator.setPreferredSize(
                new Dimension(100, 3)
        );

        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(22));
        card.add(separator);
        card.add(Box.createVerticalStrut(25));

        // Welcome heading

        JLabel welcome = new JLabel("Welcome Back");

        welcome.setFont(
                new Font("Segoe UI", Font.BOLD, 21)
        );

        welcome.setForeground(WHITE);

        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruction = new JLabel(
                "Please sign in to access your dashboard"
        );

        instruction.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        instruction.setForeground(MUTED);

        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(welcome);
        card.add(Box.createVerticalStrut(5));
        card.add(instruction);
        card.add(Box.createVerticalStrut(25));

        // Username

        JLabel usernameLabel = createLabel("USERNAME");

        username.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        styleInput(username);

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(username);
        card.add(Box.createVerticalStrut(17));

        // Password

        JLabel passwordLabel = createLabel("PASSWORD");

        password.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 42)
        );

        styleInput(password);

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(7));
        card.add(password);
        card.add(Box.createVerticalStrut(25));

        // Login button

        loginButton.setText("SIGN IN");

        loginButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 45)
        );

        styleLoginButton(loginButton);

        loginButton.addActionListener(e -> login());

        card.add(loginButton);

        // Footer

        JLabel footer = new JLabel(
                "SECURE PHARMACY MANAGEMENT SYSTEM"
        );

        footer.setFont(
                new Font("Segoe UI", Font.PLAIN, 9)
        );

        footer.setForeground(MUTED);

        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(25));
        card.add(footer);

        root.add(card);

        return root;
    }

    // =====================================
    // LABEL STYLING
    // =====================================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        label.setForeground(GOLD);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    // =====================================
    // INPUT FIELD STYLING
    // =====================================

    private void styleInput(JTextField field) {

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        field.setForeground(WHITE);

        field.setBackground(BLACK);

        field.setCaretColor(GOLD);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER, 1, true),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );
    }

    // =====================================
    // LOGIN BUTTON STYLING
    // =====================================

    private void styleLoginButton(JButton button) {

        button.setBackground(GOLD);

        button.setForeground(BLACK);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setOpaque(true);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                new EmptyBorder(12, 20, 12, 20)
        );
    }

    // =====================================
    // LOGIN AUTHENTICATION
    // =====================================

    private void login() {

        String user = username.getText().trim();

        String pass = new String(password.getPassword());

        if (user.isBlank() || pass.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both username and password.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        loginButton.setEnabled(false);

        try {

            if (new UserDao().authenticate(user, pass)) {

                dispose();

                new DashboardFrame(user).setVisible(true);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Incorrect username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                password.setText("");

                password.requestFocusInWindow();
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to MySQL.\n"
                    + "Check your database connection settings.\n\n"
                    + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            loginButton.setEnabled(true);
        }
    }
}
