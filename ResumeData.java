import java.util.ArrayList;
import java.util.List;

public class ResumeData {
    private PersonalInfo personalInfo;
    private List<Education> educationList;
    private List<WorkExperience> workList;

    public ResumeData() {
        this.educationList = new ArrayList<>();
        this.workList = new ArrayList<>();
    }

    // Constructor to init with data
    public ResumeData(PersonalInfo p, Education e, WorkExperience w) {
        this();
        this.personalInfo = p;
        if (e != null) this.educationList.add(e);
        if (w != null) this.workList.add(w);
    }

    public void setPersonalInfo(PersonalInfo info) { this.personalInfo = info; }
    public PersonalInfo getPersonalInfo() { return personalInfo; }
    
    public List<Education> getEducationList() { return educationList; }
    public List<WorkExperience> getWorkList() { return workList; }
}