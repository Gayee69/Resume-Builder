import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ResumeBuilderApp extends JFrame {

    private JTextField txtFullName, txtEmail, txtPhone, txtAddress;
    private JTextField txtSchool, txtDegree, txtGradYear;
    private JTextField txtCompany, txtJobTitle, txtDuration;
    private JComboBox<String> cmbTemplate;
    
    // New Components for Image Preview
    private JLabel lblPhotoPreview; 
    private String selectedPhotoPath = ""; 
    private JButton btnSave;

    public ResumeBuilderApp() {
        setTitle("Online Resume Builder (OOP Project)");
        setSize(600, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Resume Creator", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // --- TEMPLATE SELECTION ---
        formPanel.add(createHeader("--- Select Template ---"));
        String[] templates = { "Classic Style (Black & White)", "Modern Style (Professional Blue)" };
        cmbTemplate = new JComboBox<>(templates);
        formPanel.add(createInputRow("Design:", cmbTemplate));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // --- PHOTO UPLOAD & PREVIEW (UPDATED) ---
        formPanel.add(createHeader("--- Profile Photo ---"));
        
        JButton btnUpload = new JButton("Choose Image...");
        lblPhotoPreview = new JLabel("No Image", SwingConstants.CENTER);
        lblPhotoPreview.setPreferredSize(new Dimension(100, 100)); // Box size
        lblPhotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Border
        
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            // Filter to only show images
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
            
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                selectedPhotoPath = selectedFile.getAbsolutePath();
                
                // --- PREVIEW LOGIC ---
                // 1. Load the original image
                ImageIcon originalIcon = new ImageIcon(selectedPhotoPath);
                
                // 2. Scale it to fit our 100x100 box nicely
                Image img = originalIcon.getImage();
                Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                
                // 3. Set the scaled image to the label
                lblPhotoPreview.setIcon(new ImageIcon(newImg));
                lblPhotoPreview.setText(""); // Remove text
            }
        });

        // Layout for Photo Section
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        photoPanel.add(btnUpload);
        photoPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Spacer
        photoPanel.add(lblPhotoPreview); // The Image Box
        formPanel.add(photoPanel);

        // --- Personal Info ---
        formPanel.add(createHeader("--- Personal Information ---"));
        txtFullName = new JTextField();
        formPanel.add(createInputRow("Full Name:", txtFullName));
        txtEmail = new JTextField();
        formPanel.add(createInputRow("Email:", txtEmail));
        txtPhone = new JTextField();
        formPanel.add(createInputRow("Phone:", txtPhone));
        txtAddress = new JTextField();
        formPanel.add(createInputRow("Address:", txtAddress));

        // --- Education ---
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        formPanel.add(createHeader("--- Education ---"));
        txtSchool = new JTextField();
        formPanel.add(createInputRow("School:", txtSchool));
        txtDegree = new JTextField();
        formPanel.add(createInputRow("Degree:", txtDegree));
        txtGradYear = new JTextField();
        formPanel.add(createInputRow("Grad Year:", txtGradYear));

        // --- Work Experience ---
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        formPanel.add(createHeader("--- Work Experience ---"));
        txtCompany = new JTextField();
        formPanel.add(createInputRow("Company:", txtCompany));
        txtJobTitle = new JTextField();
        formPanel.add(createInputRow("Job Title:", txtJobTitle));
        txtDuration = new JTextField();
        formPanel.add(createInputRow("Duration:", txtDuration));

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        btnSave = new JButton("SAVE & GENERATE PDF");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(220, 45));
        
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveButton();
            }
        });

        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputRow(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 30));
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createHeader(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(150, 150, 150));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void handleSaveButton() {
        if (txtFullName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter at least a name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Pass selectedPhotoPath here
        PersonalInfo p = new PersonalInfo(txtFullName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), selectedPhotoPath);
        Education edu = new Education(txtSchool.getText(), txtDegree.getText(), txtGradYear.getText());
        WorkExperience w = new WorkExperience(txtCompany.getText(), txtJobTitle.getText(), txtDuration.getText());

        ResumeDAO dao = new ResumeDAO();
        boolean success = dao.saveResume(p, edu, w);

        if (success) {
            String selectedTemplate = (String) cmbTemplate.getSelectedItem();
             String path = System.getProperty("user.home") + "/Desktop/" + p.getFullName().replaceAll(" ", "_") + "_Resume.pdf";
             new ResumeExporter().exportToPDF(new ResumeData(p, edu, w), path, selectedTemplate);
             JOptionPane.showMessageDialog(this, "✅ Saved!\nStyle: " + selectedTemplate + "\nFile: " + path);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error saving to database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("TextComponent.arc", 10); 
            UIManager.put("Component.arc", 10); 
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }
        SwingUtilities.invokeLater(() -> new ResumeBuilderApp().setVisible(true));
    }
}