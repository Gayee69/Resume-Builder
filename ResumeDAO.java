import java.sql.*;

public class ResumeDAO {

    public boolean saveResume(PersonalInfo person, Education edu, WorkExperience work) {
        String sqlPerson = "INSERT INTO personal_info (full_name, email, phone, address, photo_path) VALUES (?, ?, ?, ?, ?)";
        String sqlEdu = "INSERT INTO education (person_id, school_name, degree, grad_year) VALUES (?, ?, ?, ?)";
        String sqlWork = "INSERT INTO work_experience (person_id, company, job_title, duration) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect()) {
            conn.setAutoCommit(false); 

            try {
                // 1. Save Personal Info + Photo
                PreparedStatement pstmt1 = conn.prepareStatement(sqlPerson, Statement.RETURN_GENERATED_KEYS);
                pstmt1.setString(1, person.getFullName());
                pstmt1.setString(2, person.getEmail());
                pstmt1.setString(3, person.getPhone());
                pstmt1.setString(4, person.getAddress());
                pstmt1.setString(5, person.getPhotoPath()); // <--- Saving Path
                pstmt1.executeUpdate();

                ResultSet rs = pstmt1.getGeneratedKeys();
                int personId = -1;
                if (rs.next()) {
                    personId = rs.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }

                // 2. Save Education
                PreparedStatement pstmt2 = conn.prepareStatement(sqlEdu);
                pstmt2.setInt(1, personId);
                pstmt2.setString(2, edu.getSchool());
                pstmt2.setString(3, edu.getDegree());
                pstmt2.setString(4, edu.getYear());
                pstmt2.executeUpdate();

                // 3. Save Work
                PreparedStatement pstmt3 = conn.prepareStatement(sqlWork);
                pstmt3.setInt(1, personId);
                pstmt3.setString(2, work.getCompany());
                pstmt3.setString(3, work.getJobTitle());
                pstmt3.setString(4, work.getDuration());
                pstmt3.executeUpdate();

                conn.commit(); 
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}