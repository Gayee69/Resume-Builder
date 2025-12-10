import java.util.ArrayList;
import java.util.List;

public class ResumeData {
    private PersonalInfo personalInfo;
    private List<Education> educationList = new ArrayList<>();
    private List<WorkExperience> workList = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private List<String> awards = new ArrayList<>();

    public ResumeData() {} // Empty constructor for loading

    public ResumeData(PersonalInfo p, Education e, WorkExperience w, List<String> skills, List<String> awards) {
        this.personalInfo = p;
        if (e != null) this.educationList.add(e);
        if (w != null) this.workList.add(w);
        if (skills != null) this.skills = skills;
        if (awards != null) this.awards = awards;
    }

    public void setPersonalInfo(PersonalInfo info) { this.personalInfo = info; }
    public PersonalInfo getPersonalInfo() { return personalInfo; }
    public List<Education> getEducationList() { return educationList; }
    public List<WorkExperience> getWorkList() { return workList; }
    public List<String> getSkills() { return skills; }
    public List<String> getAwards() { return awards; }
}