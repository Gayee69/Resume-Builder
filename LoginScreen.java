import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnRegister;

    public LoginScreen() {
        setTitle("ReBuilder - Re(sume) Builder");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        
        txtUser = new JTextField(); txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        txtPass = new JPasswordField(); txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        
        btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(40, 167, 69));
        btnLogin.setForeground(Color.WHITE);
        
        btnRegister = new JButton("Create Account");
        
        panel.add(txtUser); panel.add(txtPass); panel.add(btnLogin); panel.add(btnRegister);
        add(panel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            int userId = new UserDAO().login(txtUser.getText(), new String(txtPass.getPassword()));
            if (userId != -1) {
                dispose(); 
                new ResumeBuilderApp(userId).setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegister.addActionListener(e -> {
            if (new UserDAO().register(txtUser.getText(), new String(txtPass.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Account Created! Please Login.");
            } else {
                JOptionPane.showMessageDialog(this, "Username already taken.");
            }
        });
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatDarkLaf()); } catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}