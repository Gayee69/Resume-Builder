import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.FileOutputStream;

public class ResumeExporter {

    public void exportToPDF(ResumeData resume, String filePath, String templateName) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Only one method needed now, works for both styles
            createStandardTemplate(document, resume, templateName);

            System.out.println("✅ PDF Created Successfully: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void createStandardTemplate(Document document, ResumeData resume, String styleName) throws DocumentException {
        // 1. Choose Color
        BaseColor themeColor = styleName.contains("Modern") ? new BaseColor(0, 102, 204) : BaseColor.BLACK;
        
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, themeColor);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, themeColor);

        if (resume.getPersonalInfo() != null) {
            PersonalInfo p = resume.getPersonalInfo();

            // --- IMAGE LOGIC (ADDED) ---
            if (p.getPhotoPath() != null && !p.getPhotoPath().isEmpty()) {
                try {
                    Image img = Image.getInstance(p.getPhotoPath());
                    img.scaleToFit(100, 100); // Make it a nice small square
                    img.setAlignment(Element.ALIGN_CENTER); // Center it
                    img.setSpacingAfter(10); // Gap below image
                    
                    // Optional: Add a simple border
                    img.setBorder(Rectangle.BOX);
                    img.setBorderColor(themeColor);
                    img.setBorderWidth(1);
                    
                    document.add(img);
                } catch (Exception e) {
                    System.out.println("Could not load image (skipping): " + e.getMessage());
                }
            }
            // ---------------------------

            Paragraph name = new Paragraph(p.getFullName(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);

            Paragraph contact = new Paragraph(
                p.getEmail() + "  |  " + p.getPhone() + "\n" + p.getAddress(), 
                normalFont
            );
            contact.setAlignment(Element.ALIGN_CENTER);
            contact.setSpacingAfter(10);
            document.add(contact);
            
            LineSeparator line = new LineSeparator();
            line.setLineColor(themeColor);
            document.add(line);
            document.add(new Paragraph("\n"));
        }

        document.add(new Paragraph("EDUCATION", headerFont));
        for (Education edu : resume.getEducationList()) {
            document.add(new Paragraph(edu.getDegree(), boldFont));
            document.add(new Paragraph(edu.getSchool() + "  (" + edu.getYear() + ")", normalFont));
            document.add(new Paragraph("\n"));
        }
        
        document.add(new Paragraph("\n")); 

        document.add(new Paragraph("WORK EXPERIENCE", headerFont));
        for (WorkExperience work : resume.getWorkList()) {
            document.add(new Paragraph(work.getJobTitle(), boldFont));
            document.add(new Paragraph(work.getCompany() + "  |  " + work.getDuration(), normalFont));
            document.add(new Paragraph("\n"));
        }
    }
}