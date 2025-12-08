public import java.io.Serializable;

public class Resume implements Serializable {
    public String fullName;
    public String email;
    public String phone;
    public String skills;
    public String education;
    public String experience;
    public String imagePath; // Path to the user's photo

    public Resume(String fullName, String email, String phone, String skills, String education, String experience, String imagePath) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
        this.education = education;
        this.experience = experience;
        this.imagePath = imagePath;
    }
    
    // Manual JSON builder to avoid needing an extra GSON/Jackson JAR
    public String toJSON() {
        return String.format(
            "{\"fullName\":\"%s\", \"email\":\"%s\", \"phone\":\"%s\", \"skills\":\"%s\"}", 
            fullName, email, phone, skills
        );
    }
} {
    
}
