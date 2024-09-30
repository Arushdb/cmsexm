package edu.dei.examination.cmsexm.service; 

import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;


import edu.dei.examination.cmsexm.model.Transcript;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import edu.dei.examination.cmsexm.model.TranscriptData;
import edu.dei.examination.cmsexm.repository.TranscriptRepository;




@Service
public class TranscriptService {

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Define a logger for the class
    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);

    public void generateTranscriptPdf(Transcript transcript, OutputStream outputStream, String imagePath)  {
        Document document = new Document(PageSize.A4, 36, 36, 72, 40);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setPageEvent(new HeaderFooterPageEvent(imagePath));
         // 1. Create a PdfWriter instance
       //     PdfWriter writer1 = PdfWriter.getInstance(document, new FileOutputStream("document_with_page_numbers.pdf"));
            
            // 2. Add event for page number
            writer.setPageEvent(new PageNumberEvent());
            document.open();
            
            
            
        

            // Add spacing after opening the document
         //   document.add(new Paragraph("\n\n\n\n\n"));

            // Title Table
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100); // Set width to 100% of the page

            // Title row
            PdfPCell titleCell = new PdfPCell(new Phrase("OFFICIAL TRANSCRIPTS OF MARKS", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleTable.addCell(titleCell);

            // Date Table
            PdfPTable dateTable = new PdfPTable(1);
          //  dateTable.setWidthPercentage(100);

            String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            PdfPCell dateCell = new PdfPCell(new Phrase("Dated: " + currentDate, FontFactory.getFont(FontFactory.HELVETICA, 10)));
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            dateCell.setBorder(Rectangle.NO_BORDER);
            dateTable.addCell(dateCell);

            // Nest the date table inside the title table
            PdfPCell nestedTableCell = new PdfPCell(dateTable);
            nestedTableCell.setBorder(Rectangle.NO_BORDER);
            titleTable.addCell(nestedTableCell);

            // Add the title table to the document
            document.add(titleTable);
            document.add(new Paragraph("\n"));

            // Add transcript details
            addTranscriptTable(document, transcript);
            
          //  document.add(new Paragraph("\n"));

            // Description of grading system
            Paragraph desc1 = new Paragraph("The Institute follows a credits system and evaluation by letter grading on an 11-point scale. The Letter Grades, corresponding Grade Point Values, and their achievement level are given below:\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            document.add(desc1);

            // Add the Grade and Achievement Table
            addGradeTable(document);
            
           
         // Create a font that supports the summation symbol
         //   Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
            
            BaseFont baseFont = BaseFont.createFont("/fonts/Helvetica.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 10); // Use the correct font size
            
            // Create chunks for the summation symbol and superscript
            Chunk summationSymbol = createSummationSymbol(font);
          //  Chunk superscriptI = createSuperscript("i", font);
            Chunk superscriptTh = createSuperscript("th", font);

            // SGPA and CGPA Calculation Formulas
            Paragraph desc2 = new Paragraph();
            desc2.add(new Chunk("The procedure for calculation of SGPA is as follows:\n", font));
            desc2.add(new Chunk("                                                       SGPA (Si)=", font));
            desc2.add(summationSymbol);
            desc2.add(new Chunk("(Ci x Gi) /", font));
            desc2.add(summationSymbol);
            desc2.add(new Chunk("Ci\n(where Ci is the number of credits of the i", font));
            desc2.add(superscriptTh);
            desc2.add(new Chunk(" course and Gi", font));       
            desc2.add(new Chunk(" is the grade point scored by the student in the i", font));
            desc2.add(superscriptTh);
            desc2.add(new Chunk(" course)\n                                           CGPA = ", font));
            desc2.add(summationSymbol);
            desc2.add(new Chunk("(Ci x Si)/", font));
            desc2.add(summationSymbol);
            desc2.add(new Chunk("Ci\n(where Si", font));
            desc2.add(new Chunk(" is the SGPA of the i", font));
            desc2.add(superscriptTh);
            desc2.add(new Chunk(" Semester/Module and Ci", font));
            desc2.add(new Chunk(" is the total number of credits earned by the student)\n", font));
            desc2.add(new Chunk("Numerically, all GPA computations are expressed up to three decimal places. This ensures:\n", font));
            desc2.add(new Chunk("Greater precision in assigning credit to studentâ€™s academic achievements\n", font));
            desc2.add(new Chunk("Greater precision in discerning differences in achievements\n", font));
            desc2.add(new Chunk("More precision in conversions from one format to another\n\n", font));
            desc2.add(new Chunk("Award of Divisions: The minimum CGPA for different divisions is given in the following table.", font));

            // Add the paragraph to the document
            document.add(desc2);

          

            document.add(new Paragraph("\n"));

            // Add the Division Table
            addDivisionTable(document);
            document.add(new Paragraph("\n"));

            // CGPA description
            Paragraph desc3 = new Paragraph("Multiplication of CGPA by 10 yields the equivalent percentage marks obtained by a student.\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA, 10));
            document.add(desc3);
            
            document.newPage();

            // Details of SGPA and CGPA
          //  document.add(new Paragraph("\n"));
            Paragraph desc4 = new Paragraph("DETAILS OF SGPA OBTAINED:\n\n",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10));
            document.add(desc4);

            // Add transcript table
            // Add transcript table
            PdfPTable firstTable = createTranscriptHeaderTable(); // Only create the header once
            document.add(firstTable);
            
         // Add a gap between the header table and the transcript tables
            Paragraph gapBetweenTables = new Paragraph(""); // Creates a paragraph for spacing
            gapBetweenTables.setSpacingBefore(5); // Set spacing before the next table (adjust the value as needed)
            document.add(gapBetweenTables); // Add the gap to the document

            createTranscriptTables(transcript.getRoll_number(), document); // Call the modified table creation method
           

            // Move to a new page before the certificate section
            document.newPage();
          //  document.add(new Paragraph("\n\n\n\n\n"));

            // Certificate Section
            Paragraph certificateTitle = new Paragraph("CERTIFICATE", FontFactory.getFont(FontFactory.HELVETICA, 12));
            certificateTitle.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(certificateTitle);
            document.add(new Paragraph("\n"));

            // Certificate Text
            Paragraph paragraph = new Paragraph();
            paragraph.add(new Chunk("This is to certify and confirm that Mr./Ms. "));
            paragraph.add(new Chunk(transcript.getStudent_first_name(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            paragraph.add(new Chunk(" with Dayalbagh Educational Institute Roll No. "));
            paragraph.add(new Chunk(transcript.getRoll_number(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            paragraph.add(new Chunk(" was a bonafide student of "));
            paragraph.add(new Chunk(transcript.getProgram_name(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            paragraph.add(new Chunk(" from "));
            paragraph.add(new Chunk(transcript.getFromDate(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            paragraph.add(new Chunk(" to "));
            paragraph.add(new Chunk(transcript.getToDate(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            paragraph.add(new Chunk(" at DEI. This is a regular course conducted as per requirements prescribed by the Dayalbagh Educational Institute (Deemed to be University), Dayalbagh, Agra. He/She has successfully completed the course with all credits required for the award of the degree without any backlog.\n\n"));
            document.add(paragraph);
            document.add(new Paragraph("\n\n\n"));
         //   document.add(new Paragraph("Date:                                                                                                                                                                                                                                                                 Assistant Registrar (Exam.)\n                                                              For Registrar\n"));
            Paragraph DateTitle = new Paragraph("Date:", FontFactory.getFont(FontFactory.HELVETICA, 12));
            DateTitle.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(DateTitle);
            
            Paragraph ARTitle = new Paragraph("Assistant Registrar (Exam.)", FontFactory.getFont(FontFactory.HELVETICA, 12));
            ARTitle.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(ARTitle);
            
           
            // Close the document
            document.close();
            logger.info("Transcript PDF generated successfully.");

        } catch (DocumentException e) {
            logger.error("Error generating PDF document: ", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: ", e);
        } finally {
            // Ensure the document is closed in case of an exception
            if (document.isOpen()) {
                document.close();
            }
        }
    }
    
    
    
 // Method to create the summation symbol
    private static Chunk createSummationSymbol(Font font) {
        return new Chunk("\u2211", font); // Unicode for summation symbol (âˆ‘)
    }

    // Method to create superscript
    private static Chunk createSuperscript(String text, Font font) {
        Chunk chunk = new Chunk(text, font);
        chunk.setTextRise(3); // Adjusts the rise for superscript
        return chunk;
    }

    



    private PdfPCell createLabelCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }

    private PdfPCell createValueCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.BOTTOM);
        return cell;
    }

    
    
    private void addGradeTable(Document document) throws DocumentException { 
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 4, 2, 3, 4});

        // Define fonts
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10); // Regular font
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10); // Bold font for "Grade"

        // Add table headers
        table.addCell(getTableHeaderCell("Grade"));
        table.addCell(getTableHeaderCell("Grade Point"));
        table.addCell(getTableHeaderCell("Achievement Level"));
        table.addCell(getTableHeaderCell("Grade"));
        table.addCell(getTableHeaderCell("Grade Point"));
        table.addCell(getTableHeaderCell("Achievement Level"));

        // Grade data
        String[][] gradeData = {
                {"A", "10", "Outstanding"}, {"D", "4", "Below Average"},
                {"A-", "9", "Excellent"}, {"D-", "3", "Just Pass"},
                {"B", "8", "Very Good"}, {"E", "2", "Unsatisfactory"},
                {"B-", "7", "Good"}, {"E-", "1", "Poor"},
                {"C", "6", "Above Average"}, {"F", "0", "Very Poor"},
                {"C-", "5", "Average"}, {"", "", ""}
        };

        // Add rows to the table
        for (String[] row : gradeData) {
            // First column "Grade" - bold
            PdfPCell gradeCell1 = new PdfPCell(new Phrase(row[0], boldFont));
            table.addCell(gradeCell1);

            // Second column "Grade Point" - normal
            PdfPCell gradePointCell1 = new PdfPCell(new Phrase(row[1], normalFont));
            table.addCell(gradePointCell1);

            // Third column "Achievement Level" - normal
            PdfPCell achievementLevelCell1 = new PdfPCell(new Phrase(row[2], normalFont));
            table.addCell(achievementLevelCell1);

           
        }

        // Add the table to the document
        document.add(table);
    }

    private void addTranscriptTable(Document document, Transcript transcript) throws DocumentException {
        // Create a table with 2 columns (Label, Value)
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{10,30,10,10}); // Set column widths

        // Add student details
        addTableRow(table, "Name", transcript.getStudent_first_name());
        addTableRow(table, "Roll Number", transcript.getRoll_number());
        addTableRow(table, "Name of the Course", transcript.getProgram_name());
        addTableRow(table, "Enrollment No.", transcript.getEnrollment_number());
        addTableRow(table, "Duration of the Course", transcript.getDuration());
        addTableRow(table, "Medium of Instruction", transcript.getMedium());
        addTableRow(table, "Date of Birth", transcript.getDate_of_birth());
        addTableRow(table, "C.G.P.A.", transcript.getCgpa());

        // Add the table to the document
        document.add(table);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
    	
    	Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);  // Font for the label
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);       // Font for the value

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        table.addCell(labelCell);
        table.addCell(valueCell);
      //  table.addCell(new PdfPCell(new Phrase(label)));
      //  table.addCell(new PdfPCell(new Phrase(value)));
    }


    private void addDivisionTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidths(new float[]{3, 1});
        table.setWidthPercentage(46);
        
        // Define font for table content
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);

        table.addCell(getTableHeaderCell("Division"));
        table.addCell(getTableHeaderCell("CGPA"));

        String[][] divisionData = {
                {"First with Distinction", "8.500"},
                {"First", "6.000"},
                {"Second", "4.500"},
                {"Pass", "3.000"}
        };

        for (String[] row : divisionData) {
            for (String cellData : row) {
            	PdfPCell cell = new PdfPCell(new Phrase(cellData, font)); // Apply font
            	table.addCell(cell);
            }
        }

        document.add(table);
    }
    
    /**
     * Create the table header (called once).
     */
    private PdfPTable createTranscriptHeaderTable() throws DocumentException {
        PdfPTable table = new PdfPTable(5); // 5 columns: SEM, SESSION, SUBJECT CODE & TITLE, GRADE POINT, CREDITS
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2, 5, 1, 2}); // Set column widths
        
        // Add table headers only once
        table.addCell(getTableHeaderCell("SEM"));
        table.addCell(getTableHeaderCell("SESSION"));
        table.addCell(getTableHeaderCell("SUBJECT CODE & TITLE"));
        table.addCell(getTableHeaderCell("GRADE POINT"));
        table.addCell(getTableHeaderCell("CREDITS"));
        
        return table;
    }

    
    
    private void createTranscriptTables(String roll_number, Document document) throws DocumentException { 
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK); // Font for content
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK); // Font for bold
        List<TranscriptData> transcriptData = transcriptRepository.getTranscriptData(roll_number);
        String previousSem = "";
        int totalRows = transcriptData.size();
        int i = 0; // Start with the first row

        while (i < totalRows) { // Use a while loop for more control
            TranscriptData row = transcriptData.get(i);
            
            // If it's a new SEM, create a new table
            if (!row.getSem().equals(previousSem)) {
                // Create a new table for the current semester
                PdfPTable table = new PdfPTable(5); // 5 columns: SEM, SESSION, SUBJECT CODE & TITLE, GRADE POINT, CREDITS
                table.setWidthPercentage(100);
                table.setWidths(new float[]{1, 2, 5, 1, 2}); // Set column widths

                // Convert SEM to Roman numeral
                String semInRoman = convertToRomanNumerals(row.getSem());

                // Add SEM cell with rowspan for the current semester
                int countSemRows = getRowCountForSem(transcriptData, row.getSem());
                PdfPCell semCell = new PdfPCell(new Phrase(semInRoman, contentFont));
                semCell.setRowspan(countSemRows + 1); // Include the summary row in the rowspan
                semCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                semCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center vertically
                table.addCell(semCell);

                // Add SESSION cell with rowspan
                PdfPCell sessionCell = new PdfPCell(new Phrase(row.getSession(), contentFont));
                sessionCell.setRowspan(countSemRows + 1); // Include the summary row in the rowspan
                sessionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                sessionCell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Center vertically
                table.addCell(sessionCell);

                // Add subject-related rows for the current semester
                for (int j = 0; j < countSemRows; j++) {
                    // Ensure that 'i' is still in bounds before accessing
                    if (i < totalRows) {
                        // Add subject-related columns for the current semester
                        table.addCell(new PdfPCell(new Phrase(transcriptData.get(i).getCourseCodeName(), contentFont)));
                        table.addCell(new PdfPCell(new Phrase(transcriptData.get(i).getFinalGradePoint(), contentFont)));
                        table.addCell(new PdfPCell(new Phrase(transcriptData.get(i).getCredit(), contentFont)));
                        i++; // Increment to move to the next row in transcriptData
                    }
                }

                // Retrieve SGPA directly from the current row
                String sgpa = row.getSgpa(); 

                // Add the summary row after all subject rows
                PdfPCell summaryCell = new PdfPCell(new Phrase("S.G.P.A./M.G.P.A.: " + sgpa, boldFont)); // Update with SGPA
                summaryCell.setColspan(3); // Span across subject code, grade point, and credits
                summaryCell.setHorizontalAlignment(Element.ALIGN_RIGHT); // Align text to the right
                table.addCell(summaryCell); // This will occupy the space of the last 3 columns

                // Add empty cells for the SEM and SESSION columns in the summary row
                table.addCell(new Phrase("")); // Empty cell for SEM
                table.addCell(new Phrase("")); // Empty cell for SESSION

                // Add the completed table for the current semester to the document
                document.add(table);

                // Add a gap between tables (adjust the height as needed)
                Paragraph gap = new Paragraph(""); // You can adjust this to set the height of the gap
                gap.setSpacingBefore(5); // Add spacing before the gap
                document.add(gap); // Add the gap directly to the document

                // Update previousSem to the current semester
                previousSem = row.getSem(); // Update to the current semester
            } else {
                // If not a new SEM, just increment to move to the next row
                i++;
            }
        }
    }


    /**
     * Helper function to convert a number to Roman numerals.
     */
    private String convertToRomanNumerals(String sem) {
        int semNumber = Integer.parseInt(sem);
        String[] romanNumerals = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        
        return romanNumerals[semNumber];
    }

    /**
     * Helper function to count the number of rows for a given SEM.
     */
    private int getRowCountForSem(List<TranscriptData> transcriptData, String sem) {
        int count = 0;
        for (TranscriptData row : transcriptData) {
            if (row.getSem().equals(sem)) {
                count++;
            }
        }
        
        return count;
    }

            
            
            
            
    private PdfPCell getTableHeaderCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      //  cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        return cell;
    }

    
    
    

    public class HeaderFooterPageEvent extends PdfPageEventHelper {
        private String imagePath;

        public HeaderFooterPageEvent(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
         //   String img = imagePath;
            Image image;
            try {
                image = Image.getInstance(this.getClass().getResource(imagePath));
                image.setAlignment(Element.ALIGN_RIGHT);
                image.setAbsolutePosition(30, 700); // Adjust the position as needed
                image.scalePercent(22f, 22f);
                writer.getDirectContent().addImage(image, true);
                
                // Add vertical space dynamically after the image (estimated space based on image size)
                float imageHeight = image.getScaledHeight(); // Get the scaled height of the image
                float imageBottom = 700 - imageHeight; // Calculate the bottom Y position of the image
                float spaceBelowImage = imageBottom - 50; // Leave an additional margin (50) below the image

                // Ensure that content starts below the image by adding a placeholder paragraph or adjusting document Y position
                if (spaceBelowImage > 0) {
                    document.add(new Paragraph("\n\n\n\n\n")); // Add new lines or padding to push content
                }
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }

           
        }

       
    }
    

    

  


      
    

    
 
     // Fetch Transcript data using a MySQL query
        public Transcript getTranscriptByRollNumber(String roll_number) {
            String sql ="SELECT substring(sp.registered_from_session,1,4) FromDate,substring(sp.passed_to_session,1,4) ToDate,'4' as duration,'ENGLISH' as medium, srsh.roll_number,sm.student_first_name,pm.program_name,sp.enrollment_number,sm.date_of_birth,sp.cgpa"
            		+ " FROM cms_live.student_registration_semester_header srsh JOIN  cms_live.program_course_header pch ON"
            		+ " srsh.program_course_key = pch.program_course_key JOIN cms_live.student_program sp ON "
            		+ " srsh.roll_number = sp.roll_number AND pch.program_id = sp.program_id AND srsh.entity_id = sp.entity_id  "
            		+ " AND pch.specialization_id = sp.specialization_id AND pch.branch_id =sp.branch_id"
            		+ " JOIN cms_live.program_master pm ON pm.program_id = sp.program_id"
            		+ " JOIN cms_live.student_master sm ON sm.enrollment_number = sp.enrollment_number "
            		+ " WHERE sp.program_status = 'PAS'  AND srsh.roll_number = ? limit 1";

            // Using JdbcTemplate to query for a single result
            return jdbcTemplate.queryForObject(sql, new Object[]{roll_number}, new RowMapper<Transcript>() {
                @Override
                public Transcript mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Transcript transcript = new Transcript();
                    transcript.setRoll_number(rs.getString("roll_number"));
                    transcript.setStudent_first_name(rs.getString("student_first_name"));
                    transcript.setProgram_name(rs.getString("program_name"));
                    transcript.setEnrollment_number(rs.getString("enrollment_number"));
                    transcript.setDuration(rs.getString("duration"));
                    transcript.setMedium(rs.getString("medium"));
                    transcript.setDate_of_birth(rs.getString("date_of_birth"));
                    transcript.setCgpa(rs.getString("cgpa"));
                    transcript.setFromDate(rs.getString("FromDate"));
                    transcript.setToDate(rs.getString("ToDate"));
                    return transcript;
                }
            });
            
        }
}
       