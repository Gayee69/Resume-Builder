public class PersonalInfo {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String photoPath; // <--- The new field

    // Constructor with 5 arguments
    public PersonalInfo(String fullName, String email, String phone, String address, String photoPath) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.photoPath = photoPath;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getPhotoPath() { return photoPath; }
}