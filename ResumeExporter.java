import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.FileOutputStream;
import java.util.List;

public class ResumeExporter {

    public void exportToPDF(ResumeData resume, String filePath, String templateName) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            if (templateName.contains("Riya")) {
                createRiyaTemplate(document, resume);
            } else {
                createStandardTemplate(document, resume, templateName);
            }
            System.out.println("✅ PDF Created: " + filePath);
        } catch (Exception e) { e.printStackTrace(); } 
        finally { document.close(); }
    }

    private void createRiyaTemplate(Document document, ResumeData resume) throws DocumentException {
        BaseColor darkBlue = new BaseColor(44, 62, 80);   
        BaseColor sidebarGrey = new BaseColor(230, 230, 230);
        
        // Header
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(darkBlue);
        headerCell.setPadding(30);
        headerCell.setBorder(Rectangle.NO_BORDER);
        
        if (resume.getPersonalInfo() != null) {
            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, BaseColor.WHITE);
            Paragraph name = new Paragraph(resume.getPersonalInfo().getFullName().toUpperCase(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(name);
        }
        headerTable.addCell(headerCell);
        document.add(headerTable);

        // Split Layout
        PdfPTable mainTable = new PdfPTable(2);
        mainTable.setWidthPercentage(100);
        mainTable.setWidths(new float[]{3.5f, 6.5f});

        // Sidebar
        PdfPCell leftCell = new PdfPCell();
        leftCell.setBackgroundColor(sidebarGrey);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftCell.setPadding(20);
        leftCell.setMinimumHeight(600);

        if (resume.getPersonalInfo() != null && resume.getPersonalInfo().getPhotoPath() != null) {
            try {
                Image img = Image.getInstance(resume.getPersonalInfo().getPhotoPath());
                img.scaleToFit(120, 120);
                img.setAlignment(Element.ALIGN_CENTER);
                img.setBorder(Rectangle.BOX);
                img.setBorderColor(darkBlue);
                img.setBorderWidth(3);
                leftCell.addElement(img);
            } catch (Exception e) {}
        }

        leftCell.addElement(new Paragraph("\n"));
        addSidebarSection(leftCell, "CONTACT", resume);
        leftCell.addElement(new Paragraph("\n"));
        addSidebarList(leftCell, "KEY SKILLS", resume.getSkills());
        mainTable.addCell(leftCell);

        // Content
        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setPadding(20);

        addSectionHeader(rightCell, "EDUCATION");
        for (Education edu : resume.getEducationList()) {
            rightCell.addElement(new Paragraph("• " + edu.getDegree(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            rightCell.addElement(new Paragraph("   " + edu.getSchool() + " | " + edu.getYear()));
            rightCell.addElement(new Paragraph("\n"));
        }

        addSectionHeader(rightCell, "WORK EXPERIENCE");
        for (WorkExperience work : resume.getWorkList()) {
            rightCell.addElement(new Paragraph("• " + work.getJobTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            rightCell.addElement(new Paragraph("   " + work.getCompany() + " | " + work.getDuration()));
            rightCell.addElement(new Paragraph("\n"));
        }

        if (resume.getAwards() != null && !resume.getAwards().isEmpty()) {
             addSectionHeader(rightCell, "AWARDS & HONORS");
             for (String award : resume.getAwards()) {
                 if(!award.trim().isEmpty()) rightCell.addElement(new Paragraph("• " + award));
             }
        }
        
        mainTable.addCell(rightCell);
        document.add(mainTable);
    }

    private void addSidebarSection(PdfPCell cell, String title, ResumeData resume) {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
        cell.addElement(new Paragraph(title, titleFont));
        Paragraph line = new Paragraph(); line.add(new LineSeparator()); cell.addElement(line);
        if (resume.getPersonalInfo() != null) {
            cell.addElement(new Paragraph("\n📞 " + resume.getPersonalInfo().getPhone(), textFont));
            cell.addElement(new Paragraph("📧 " + resume.getPersonalInfo().getEmail(), textFont));
            cell.addElement(new Paragraph("📍 " + resume.getPersonalInfo().getAddress(), textFont));
        }
    }

    private void addSidebarList(PdfPCell cell, String title, List<String> items) {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
        cell.addElement(new Paragraph(title, titleFont));
        Paragraph line = new Paragraph(); line.add(new LineSeparator()); cell.addElement(line);
        cell.addElement(new Paragraph("\n"));
        if (items != null) {
            for (String item : items) {
                if (!item.trim().isEmpty()) cell.addElement(new Paragraph("• " + item, textFont));
            }
        }
    }

    private void addSectionHeader(PdfPCell cell, String title) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLACK);
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(10); p.setSpacingAfter(5);
        cell.addElement(p);
        Paragraph line = new Paragraph(); line.add(new LineSeparator()); cell.addElement(line);
        cell.addElement(new Paragraph("\n"));
    }

    private void createStandardTemplate(Document document, ResumeData resume, String styleName) throws DocumentException {
        BaseColor themeColor = styleName.contains("Modern") ? new BaseColor(0, 102, 204) : BaseColor.BLACK;
        Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 26, themeColor);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        
        if (resume.getPersonalInfo() != null) {
            PersonalInfo p = resume.getPersonalInfo();
            if (p.getPhotoPath() != null && !p.getPhotoPath().isEmpty()) {
                try {
                    Image img = Image.getInstance(p.getPhotoPath());
                    img.scaleToFit(80, 80);
                    img.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                } catch (Exception e) {}
            }
            Paragraph name = new Paragraph(p.getFullName(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            document.add(name);
            document.add(new Paragraph(p.getEmail() + " | " + p.getPhone(), normalFont));
            LineSeparator line = new LineSeparator(); line.setLineColor(themeColor);
            document.add(line);
        }

        document.add(new Paragraph("\nEDUCATION", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, themeColor)));
        for (Education edu : resume.getEducationList()) {
            document.add(new Paragraph(edu.getDegree() + " - " + edu.getSchool() + " (" + edu.getYear() + ")"));
        }
        document.add(new Paragraph("\nWORK EXPERIENCE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, themeColor)));
        for (WorkExperience work : resume.getWorkList()) {
            document.add(new Paragraph(work.getJobTitle() + " at " + work.getCompany() + " (" + work.getDuration() + ")"));
        }
        document.add(new Paragraph("\nSKILLS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, themeColor)));
        for (String s : resume.getSkills()) if(!s.isEmpty()) document.add(new Paragraph("• " + s));
        
        document.add(new Paragraph("\nAWARDS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, themeColor)));
        for (String a : resume.getAwards()) if(!a.isEmpty()) document.add(new Paragraph("• " + a));
    }
}