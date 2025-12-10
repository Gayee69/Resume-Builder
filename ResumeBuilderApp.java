import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ResumeBuilderApp extends JFrame {

    private JTextField txtFullName, txtEmail, txtPhone, txtAddress;
    private JTextField txtSchool, txtDegree, txtGradYear;
    private JTextField txtCompany, txtJobTitle, txtDuration;
    private JTextArea txtSkills, txtAwards;
    private JComboBox<String> cmbTemplate;
    private JLabel lblPhotoPreview; 
    private String selectedPhotoPath = ""; 
    private JButton btnSave;
    private int currentUserId;

    public ResumeBuilderApp(int userId) {
        this.currentUserId = userId;
        setTitle("ReBuilder - User ID: " + userId);
        setSize(750, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. HEADER SECTION (Fixed Alignment) ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        // A. Title (Center)
        JLabel lblTitle = new JLabel("Resume Builder", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        // B. Right Side Buttons (History + Logout)
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnHistory = new JButton("Load History");
        btnHistory.setBackground(new Color(23, 162, 184)); // Cyan
        btnHistory.setForeground(Color.WHITE);
        btnHistory.setFocusPainted(false);
        btnHistory.addActionListener(e -> showHistoryDialog());
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69)); // Red
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });
        
        rightHeader.add(btnHistory);
        rightHeader.add(btnLogout);

        // C. Left Side Dummy (To balance the Right side so Title stays centered)
        JPanel leftDummy = new JPanel();
        leftDummy.setPreferredSize(new Dimension(rightHeader.getPreferredSize().width, 10)); 
        leftDummy.setOpaque(false);

        // Assemble Header
        headerPanel.add(leftDummy, BorderLayout.WEST);
        headerPanel.add(lblTitle, BorderLayout.CENTER);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        add(headerPanel, BorderLayout.NORTH);


        // --- 2. MAIN FORM ---
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        // A. Template & Photo (Improved Layout)
        // We use a GridBagLayout for better control than GridLayout
        JPanel topSection = new JPanel(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Template Selector (Weight 1.0 = takes up more space)
        gbc.gridx = 0; gbc.weightx = 0.7; gbc.insets = new Insets(0, 0, 0, 15);
        JPanel templatePanel = new JPanel(new BorderLayout());
        templatePanel.add(createHeader("Select Template"), BorderLayout.NORTH);
        String[] templates = { "Riya Style (Blue/Gray Sidebar)", "Classic Style (Black & White)", "Modern Style (Blue)" };
        cmbTemplate = new JComboBox<>(templates);
        templatePanel.add(cmbTemplate, BorderLayout.CENTER);
        topSection.add(templatePanel, gbc);
        
        // Photo Upload (Weight 0 = takes up only needed space)
        gbc.gridx = 1; gbc.weightx = 0.3; gbc.insets = new Insets(0, 0, 0, 0);
        JPanel photoContainer = new JPanel(new BorderLayout());
        photoContainer.add(createHeader(" "), BorderLayout.NORTH); // Spacer to align with text
        
        JPanel photoInner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnUpload = new JButton("Upload Photo");
        lblPhotoPreview = new JLabel("No Image", SwingConstants.CENTER);
        lblPhotoPreview.setPreferredSize(new Dimension(100, 100));
        lblPhotoPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectedPhotoPath = file.getAbsolutePath();
                loadPhotoPreview(selectedPhotoPath);
            }
        });
        
        photoInner.add(btnUpload);
        photoInner.add(lblPhotoPreview);
        photoContainer.add(photoInner, BorderLayout.CENTER);
        topSection.add(photoContainer, gbc);

        // Align Top Section Left
        topSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        topSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        formPanel.add(topSection);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // B. Personal Info
        formPanel.add(createHeader("Personal Information"));
        txtFullName = new JTextField(); txtEmail = new JTextField();
        formPanel.add(createTwoColumnRow("Full Name:", txtFullName, "Email:", txtEmail));
        txtPhone = new JTextField(); txtAddress = new JTextField();
        formPanel.add(createTwoColumnRow("Phone:", txtPhone, "Address:", txtAddress));

        // C. Education
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        formPanel.add(createHeader("Education"));
        txtSchool = new JTextField(); txtDegree = new JTextField();
        formPanel.add(createTwoColumnRow("School/University:", txtSchool, "Degree:", txtDegree));
        txtGradYear = new JTextField();
        formPanel.add(createTwoColumnRow("Grad Year:", txtGradYear, "", new JLabel())); 

        // D. Work Experience
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        formPanel.add(createHeader("Professional Experience"));
        txtCompany = new JTextField(); txtJobTitle = new JTextField();
        formPanel.add(createTwoColumnRow("Company:", txtCompany, "Job Title:", txtJobTitle));
        txtDuration = new JTextField();
        formPanel.add(createTwoColumnRow("Duration:", txtDuration, "", new JLabel())); 

        // E. Skills & Awards
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(createHeader("Skills & Awards (One per line)"));
        
        JPanel listPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        listPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtSkills = new JTextArea(5, 20);
        JScrollPane scrollSkills = new JScrollPane(txtSkills);
        scrollSkills.setBorder(BorderFactory.createTitledBorder("Skills"));
        listPanel.add(scrollSkills);

        txtAwards = new JTextArea(5, 20);
        JScrollPane scrollAwards = new JScrollPane(txtAwards);
        scrollAwards.setBorder(BorderFactory.createTitledBorder("Awards / Honors"));
        listPanel.add(scrollAwards);
        
        formPanel.add(listPanel);

        add(new JScrollPane(formPanel), BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        btnSave = new JButton("SAVE & GENERATE");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(250, 45));
        
        btnSave.addActionListener(e -> handleSaveButton());
        buttonPanel.add(btnSave);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- LOGIC METHODS ---

    private void handleSaveButton() {
        if (txtFullName.getText().isEmpty()) return;

        PersonalInfo p = new PersonalInfo(txtFullName.getText(), txtEmail.getText(), txtPhone.getText(), txtAddress.getText(), selectedPhotoPath);
        Education edu = new Education(txtSchool.getText(), txtDegree.getText(), txtGradYear.getText());
        WorkExperience w = new WorkExperience(txtCompany.getText(), txtJobTitle.getText(), txtDuration.getText());
        List<String> skills = Arrays.asList(txtSkills.getText().split("\\n"));
        List<String> awards = Arrays.asList(txtAwards.getText().split("\\n"));

        if (new ResumeDAO().saveResume(currentUserId, p, edu, w, skills, awards)) {
            String template = (String) cmbTemplate.getSelectedItem();
            String path = System.getProperty("user.home") + "/Desktop/" + p.getFullName().replaceAll(" ", "_") + "_Resume.pdf";
            new ResumeExporter().exportToPDF(new ResumeData(p, edu, w, skills, awards), path, template);
            JOptionPane.showMessageDialog(this, "✅ Saved!\nPDF: " + path);
        }
    }

    private void showHistoryDialog() {
        ResumeDAO dao = new ResumeDAO();
        Map<Integer, String> resumes = dao.getUserResumes(currentUserId);

        if (resumes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No saved resumes found.");
            return;
        }

        String[] displayNames = resumes.values().toArray(new String[0]);
        String selectedName = (String) JOptionPane.showInputDialog(
                this, "Select a resume to load:", "Load History", 
                JOptionPane.QUESTION_MESSAGE, null, displayNames, displayNames[0]);

        if (selectedName != null) {
            int selectedId = -1;
            for (Map.Entry<Integer, String> entry : resumes.entrySet()) {
                if (entry.getValue().equals(selectedName)) {
                    selectedId = entry.getKey();
                    break;
                }
            }
            if (selectedId != -1) {
                ResumeData data = dao.getResumeDetails(selectedId);
                populateForm(data);
                JOptionPane.showMessageDialog(this, "Resume Loaded Successfully!");
            }
        }
    }

    private void populateForm(ResumeData data) {
        if (data.getPersonalInfo() != null) {
            PersonalInfo p = data.getPersonalInfo();
            txtFullName.setText(p.getFullName());
            txtEmail.setText(p.getEmail());
            txtPhone.setText(p.getPhone());
            txtAddress.setText(p.getAddress());
            selectedPhotoPath = p.getPhotoPath();
            loadPhotoPreview(selectedPhotoPath);
        }
        if (!data.getEducationList().isEmpty()) {
            Education e = data.getEducationList().get(0);
            txtSchool.setText(e.getSchool());
            txtDegree.setText(e.getDegree());
            txtGradYear.setText(e.getYear());
        }
        if (!data.getWorkList().isEmpty()) {
            WorkExperience w = data.getWorkList().get(0);
            txtCompany.setText(w.getCompany());
            txtJobTitle.setText(w.getJobTitle());
            txtDuration.setText(w.getDuration());
        }
        txtSkills.setText(String.join("\n", data.getSkills()));
        txtAwards.setText(String.join("\n", data.getAwards()));
    }

    private void loadPhotoPreview(String path) {
        if (path != null && !path.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                lblPhotoPreview.setIcon(icon);
                lblPhotoPreview.setText("");
            } catch (Exception e) {
                lblPhotoPreview.setText("Error");
            }
        }
    }

    // --- UI HELPERS ---

    private Component createHeader(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(180, 180, 180));
        panel.add(label, BorderLayout.WEST);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); 
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createTwoColumnRow(String label1, JComponent field1, String label2, JComponent field2) {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JPanel left = new JPanel(new BorderLayout(0, 5));
        if(!label1.isEmpty()) left.add(new JLabel(label1), BorderLayout.NORTH);
        left.add(field1, BorderLayout.CENTER);
        
        JPanel right = new JPanel(new BorderLayout(0, 5));
        if(!label2.isEmpty()) right.add(new JLabel(label2), BorderLayout.NORTH);
        right.add(field2, BorderLayout.CENTER);

        panel.add(left); panel.add(right);
        return panel;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new FlatDarkLaf()); UIManager.put("TextComponent.arc", 10); } 
        catch (Exception ex) {}
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}