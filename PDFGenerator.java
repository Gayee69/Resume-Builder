import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

public class PDFGenerator {

    public static void createResumePDF(Resume resume, String dest) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();

            // 1. Add Image (Top Right or Left)
            if (resume.imagePath != null && !resume.imagePath.isEmpty()) {s
                Image img = Image.getInstance(resume.imagePath);
                img.scaleToFit(100, 100); // Resize image
                img.setAlignment(Element.ALIGN_RIGHT);
                document.add(img);
            }

            // 2. Header
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph name = new Paragraph(resume.fullName, titleFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);

            Paragraph contact = new Paragraph(resume.email + " | " + resume.phone);
            contact.setAlignment(Element.ALIGN_CENTER);
            contact.setSpacingAfter(20);
            document.add(contact);

            // 3. Sections
            addSection(document, "Education", resume.education);
            addSection(document, "Work Experience", resume.experience);
            addSection(document, "Skills", resume.skills);

            document.close();
            System.out.println("PDF Created Successfully at: " + dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addSection(Document doc, String title, String content) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.DARK_GRAY);
        Paragraph sectionHeader = new Paragraph(title, headerFont);
        sectionHeader.setSpacingBefore(10);
        doc.add(sectionHeader);
        
        // Add a line separator
        doc.add(new Paragraph("________________________________________________"));

        Paragraph sectionContent = new Paragraph(content);
        sectionContent.setSpacingBefore(5);
        doc.add(sectionContent);
    }
}