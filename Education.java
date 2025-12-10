public class Education {
    private String school;
    private String degree;
    private String year;

    public Education(String school, String degree, String year) {
        this.school = school;
        this.degree = degree;
        this.year = year;
    }

    public String getSchool() { return school; }
    public String getDegree() { return degree; }
    public String getYear() { return year; }
}