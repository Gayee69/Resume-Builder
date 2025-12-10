import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ResumeDAO {

    // --- 1. SAVE RESUME (Linked to User ID) ---
    public boolean saveResume(int userId, PersonalInfo person, Education edu, WorkExperience work, List<String> skills, List<String> awards) {
        String sqlPerson = "INSERT INTO personal_info (user_id, full_name, email, phone, address, photo_path) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEdu = "INSERT INTO education (person_id, school_name, degree, grad_year) VALUES (?, ?, ?, ?)";
        String sqlWork = "INSERT INTO work_experience (person_id, company, job_title, duration) VALUES (?, ?, ?, ?)";
        String sqlSkill = "INSERT INTO skills (person_id, skill_name) VALUES (?, ?)";
        String sqlAward = "INSERT INTO awards (person_id, award_name) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.connect()) {
            conn.setAutoCommit(false); // Start Transaction

            try {
                // A. Save Personal Info
                PreparedStatement pstmt1 = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS);
                pstmt1.setInt(1, userId);
                pstmt1.setString(2, person.getFullName());
                pstmt1.setString(3, person.getEmail());
                pstmt1.setString(4, person.getPhone());
                pstmt1.setString(5, person.getAddress());
                pstmt1.setString(6, person.getPhotoPath());
                pstmt1.executeUpdate();

                ResultSet rs = pstmt1.getGeneratedKeys();
                int personId = -1;
                if (rs.next()) personId = rs.getInt(1);
                else throw new SQLException("No ID obtained.");

                // B. Save Education
                PreparedStatement pstmt2 = conn.prepareStatement(sqlEdu);
                pstmt2.setInt(1, personId);
                pstmt2.setString(2, edu.getSchool());
                pstmt2.setString(3, edu.getDegree());
                pstmt2.setString(4, edu.getYear());
                pstmt2.executeUpdate();

                // C. Save Work
                PreparedStatement pstmt3 = conn.prepareStatement(sqlWork);
                pstmt3.setInt(1, personId);
                pstmt3.setString(2, work.getCompany());
                pstmt3.setString(3, work.getJobTitle());
                pstmt3.setString(4, work.getDuration());
                pstmt3.executeUpdate();

                // D. Save Skills (Batch)
                if (skills != null) {
                    PreparedStatement psSkill = conn.prepareStatement(sqlSkill);
                    for (String s : skills) {
                        if (!s.trim().isEmpty()) {
                            psSkill.setInt(1, personId);
                            psSkill.setString(2, s);
                            psSkill.addBatch();
                        }
                    }
                    psSkill.executeBatch();
                }

                // E. Save Awards (Batch)
                if (awards != null) {
                    PreparedStatement psAward = conn.prepareStatement(sqlAward);
                    for (String a : awards) {
                        if (!a.trim().isEmpty()) {
                            psAward.setInt(1, personId);
                            psAward.setString(2, a);
                            psAward.addBatch();
                        }
                    }
                    psAward.executeBatch();
                }

                conn.commit(); // Commit Transaction
                return true;

            } catch (SQLException e) {
                conn.rollback(); // Undo if error
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 2. GET HISTORY LIST (For Popup) ---
    public Map<Integer, String> getUserResumes(int userId) {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT id, full_name, phone FROM personal_info WHERE user_id = ? ORDER BY id DESC";
        
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Display Format: "John Doe (555-0199)"
                map.put(rs.getInt("id"), rs.getString("full_name") + " (" + rs.getString("phone") + ")");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    // --- 3. LOAD FULL RESUME (For Editing) ---
    public ResumeData getResumeDetails(int personId) {
        ResumeData data = new ResumeData();
        
        try (Connection conn = DatabaseHelper.connect()) {
            // A. Personal Info
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM personal_info WHERE id = ?");
            ps1.setInt(1, personId);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                data.setPersonalInfo(new PersonalInfo(
                    rs1.getString("full_name"),
                    rs1.getString("email"),
                    rs1.getString("phone"),
                    rs1.getString("address"),
                    rs1.getString("photo_path")
                ));
            }

            // B. Education
            PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM education WHERE person_id = ?");
            ps2.setInt(1, personId);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()) {
                data.getEducationList().add(new Education(
                    rs2.getString("school_name"), rs2.getString("degree"), rs2.getString("grad_year")
                ));
            }

            // C. Work
            PreparedStatement ps3 = conn.prepareStatement("SELECT * FROM work_experience WHERE person_id = ?");
            ps3.setInt(1, personId);
            ResultSet rs3 = ps3.executeQuery();
            while(rs3.next()) {
                data.getWorkList().add(new WorkExperience(
                    rs3.getString("company"), rs3.getString("job_title"), rs3.getString("duration")
                ));
            }

            // D. Skills
            PreparedStatement ps4 = conn.prepareStatement("SELECT skill_name FROM skills WHERE person_id = ?");
            ps4.setInt(1, personId);
            ResultSet rs4 = ps4.executeQuery();
            while(rs4.next()) data.getSkills().add(rs4.getString("skill_name"));

            // E. Awards
            PreparedStatement ps5 = conn.prepareStatement("SELECT award_name FROM awards WHERE person_id = ?");
            ps5.setInt(1, personId);
            ResultSet rs5 = ps5.executeQuery();
            while(rs5.next()) data.getAwards().add(rs5.getString("award_name"));

        } catch (SQLException e) { e.printStackTrace(); }
        
        return data;
    }
}