import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class Main extends JFrame {

    // UI Components
    private JTextField txtName, txtEmail, txtPhone, txtImagePath;
    private JTextArea areaEducation, areaExperience, areaSkills;
    private JLabel lblImagePreview;
    private JButton btnUpload, btnGenerate;

    public Main() {
        setTitle("Online Resume Builder [OOP Project]");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 1. Setup Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize Fields
        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtImagePath = new JTextField();
        txtImagePath.setEditable(false);
        
        areaEducation = new JTextArea(3, 20);
        areaExperience = new JTextArea(3, 20);
        areaSkills = new JTextArea(3, 20);

        // Add to Panel
        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);
        
        formPanel.add(new JLabel("Profile Photo:"));
        JPanel photoPanel = new JPanel(new BorderLayout());
        btnUpload = new JButton("Upload Image");
        photoPanel.add(txtImagePath, BorderLayout.CENTER);
        photoPanel.add(btnUpload, BorderLayout.EAST);
        formPanel.add(photoPanel);

        formPanel.add(new JLabel("Education:"));
        formPanel.add(new JScrollPane(areaEducation));
        formPanel.add(new JLabel("Experience:"));
        formPanel.add(new JScrollPane(areaExperience));
        formPanel.add(new JLabel("Skills:"));
        formPanel.add(new JScrollPane(areaSkills));

        // 2. Button Panel
        JPanel btnPanel = new JPanel();
        btnGenerate = new JButton("Save & Generate PDF");
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 14));
        btnPanel.add(btnGenerate);

        add(new JLabel("RESUME BUILDER", SwingConstants.CENTER), BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // 3. Action Listeners
        
        // Image Upload Logic
        btnUpload.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                txtImagePath.setText(selectedFile.getAbsolutePath());
            }
        });

        // Generate Logic
        btnGenerate.addActionListener((ActionEvent e) -> {
            // Collect Data
            Resume resume = new Resume(
                txtName.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                areaSkills.getText(),
                areaEducation.getText(),
                areaExperience.getText(),
                txtImagePath.getText()
            );

            // A. Save to Firebase
            FirebaseHelper.saveResume(resume);

            // B. Generate PDF
            String outputPath = "Resume_" + System.currentTimeMillis() + ".pdf";
            PDFGenerator.createResumePDF(resume, outputPath);
            
            JOptionPane.showMessageDialog(this, "Resume Saved & PDF Generated!\nFile: " + outputPath);
        });
    }

    public static void main(String[] args) {
        // Setup FlatLaf Theme
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}