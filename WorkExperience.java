public class WorkExperience {
    private String company;
    private String jobTitle;
    private String duration;

    public WorkExperience(String company, String jobTitle, String duration) {
        this.company = company;
        this.jobTitle = jobTitle;
        this.duration = duration;
    }

    public String getCompany() { return company; }
    public String getJobTitle() { return jobTitle; }
    public String getDuration() { return duration; }
}