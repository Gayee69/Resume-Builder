import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirebaseHelper {
    // REPLACE WITH YOUR FIREBASE URL (Must end in /)
    private static final String FIREBASE_URL = "https://resumebuilder-app-5ad38-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public static void saveResume(Resume resume) {
        try {
            URL url = new URL(FIREBASE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST"); // POST creates a new unique ID
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            // Convert object to JSON string manually
            String jsonInputString = resume.toJSON();

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Firebase Response Code: " + code); // 200 is good

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}