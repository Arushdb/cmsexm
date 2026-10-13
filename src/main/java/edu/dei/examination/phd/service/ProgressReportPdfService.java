package edu.dei.examination.phd.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.phd.model.Achievement;
import edu.dei.examination.phd.model.ConferenceAttended;
import edu.dei.examination.phd.model.DepartmentRoleAssignment;
import edu.dei.examination.phd.model.FacultyRoleAssignment;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ProgressReportPdf;
import edu.dei.examination.phd.model.ProgressWork;
import edu.dei.examination.phd.model.Publication;
import edu.dei.examination.phd.model.Report;
import edu.dei.examination.phd.model.ReviewHistory;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;

import edu.dei.examination.phd.model.Visit;

import edu.dei.examination.phd.repository.AchievementRepository;
import edu.dei.examination.phd.repository.ConferenceAttendedRepository;
import edu.dei.examination.phd.repository.DepartmentRoleAssignmentRepository;
import edu.dei.examination.phd.repository.FacultyRoleRepository;
import edu.dei.examination.phd.repository.ProgressReportPdfRepository;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.ProgressWorkRepository;
import edu.dei.examination.phd.repository.PublicationRepository;
import edu.dei.examination.phd.repository.ReportRepository;
import edu.dei.examination.phd.repository.ReviewHistoryRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.VisitRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;

import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;


@Service
public class ProgressReportPdfService {

    /*
     * Change this location according to your server.
     *
     * Linux:
     * /opt/phd/progress-report-pdfs
     *
     * Windows example:
     * D:/phd/progress-report-pdfs
     */
//    private final String pdfBasePath =
//            "/opt/phd/progress-report-pdfs";
    
    @Value("${phd.pdf.storage-path}")
    private String pdfBasePath;

    private final ProgressReportRepository progressReportRepository;
    private final ReportRepository reportRepository;

    private final ProgressWorkRepository progressWorkRepository;
    private final PublicationRepository publicationRepository;
    private final VisitRepository visitRepository;
    private final ConferenceAttendedRepository conferenceRepository;
    private final AchievementRepository achievementRepository;

    private final ReviewHistoryRepository historyRepository;
    private final ProgressReportPdfRepository pdfRepository;
    private final RoleRepository roleRepository;
    private final ScholarsRepository scholarsRepository;
    private final UserRepository userRepository;
    private final ReviewHistoryRepository reviewHistoryRepository ;
    private final DepartmentRoleAssignmentRepository departmentRoleAssignmentRepository;
    private final FacultyRoleRepository facultyRoleRepository;
    private Font unicodeNormalFont;
    private Font unicodeBoldFont;

    public ProgressReportPdfService(
            ProgressReportRepository progressReportRepository,
            ReportRepository reportRepository,
            ProgressWorkRepository progressWorkRepository,
            PublicationRepository publicationRepository,
            VisitRepository visitRepository,
            ConferenceAttendedRepository conferenceRepository,
            AchievementRepository achievementRepository,
            ReviewHistoryRepository historyRepository,
            ProgressReportPdfRepository pdfRepository,
            RoleRepository roleRepository,
            ScholarsRepository scholarsRepository,
            UserRepository userRepository,
            ReviewHistoryRepository reviewHistoryRepository,
            DepartmentRoleAssignmentRepository departmentRoleAssignmentRepository,
            FacultyRoleRepository facultyRoleRepository 
            
    		) {

        this.progressReportRepository =
                progressReportRepository;

        this.reportRepository =
                reportRepository;

        this.progressWorkRepository =
                progressWorkRepository;

        this.publicationRepository =
                publicationRepository;

        this.visitRepository =
                visitRepository;

        this.conferenceRepository =
                conferenceRepository;

        this.achievementRepository =
                achievementRepository;

        this.historyRepository =
                historyRepository;

        this.pdfRepository =
                pdfRepository;

        this.roleRepository =
                roleRepository;
        
        this.scholarsRepository=scholarsRepository;
        this.userRepository=userRepository;
        this.reviewHistoryRepository=reviewHistoryRepository;
        this.departmentRoleAssignmentRepository=departmentRoleAssignmentRepository;
        this.facultyRoleRepository =facultyRoleRepository;
    }


    // =========================================================
    // MAIN METHOD
    // =========================================================

    @Transactional(readOnly = true)
    public ProgressReportPdf generatePdf(
            Integer progressReportId,
            Integer reviewHistoryId,
            String action,
            Integer generatedBy) throws Exception {

        ProgressReport progressReport =
                progressReportRepository
                        .findById(progressReportId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Progress report not found: "
                                        + progressReportId));

        /*
         * IMPORTANT:
         *
         * We take the snapshot NOW.
         * Later modifications to thFe report will not change
         * this PDF.
         */

        Integer maxVersion =
                pdfRepository
                        .findMaxVersionByReportId(
                                progressReportId);

        int versionNo =
                maxVersion == null
                        ? 1
                        : maxVersion + 1;


        String safeAction =
                action == null
                        ? "UNKNOWN"
                        : action
                            .toUpperCase()
                            .replaceAll(
                                "[^A-Z0-9_]+",
                                "_");


        String directory =
                pdfBasePath
                + File.separator
                + "report-"
                + progressReportId;


        File directoryFile =
                new File(directory);

        if (!directoryFile.exists()) {

            if (!directoryFile.mkdirs()
                    && !directoryFile.exists()) {

                throw new RuntimeException(
                        "Unable to create PDF directory: "
                        + directory);
            }
        }


        String fileName =
                "progress_report_"
                + progressReportId
                + "_v"
                + versionNo
                + "_"
                + safeAction
                + ".pdf";


        String filePath =
                directory
                + File.separator
                + fileName;


        // Generate physical PDF
        generatePdfFile(
                progressReport,
                versionNo,
                action,
                generatedBy,
                filePath);


        // Save PDF metadata
        ProgressReportPdf pdf =
                new ProgressReportPdf();

        pdf.setReportId(progressReportId);
        pdf.setReviewHistoryId(reviewHistoryId);
        pdf.setVersionNo(versionNo);
        pdf.setAction(action);
        pdf.setFileName(fileName);
        pdf.setFilePath(filePath);
        pdf.setGeneratedBy(generatedBy);
        pdf.setGeneratedAt(
                LocalDateTime.now());


        return pdfRepository.save(pdf);
    }


    // =========================================================
    // CREATE PDF
    // =========================================================

    private void generatePdfFile(
            ProgressReport progressReport,
            Integer versionNo,
            String action,
            Integer generatedBy,
            String filePath)
            throws Exception {


        Document document =
                new Document(
                        PageSize.A4,
                        36,
                        36,
                        45,
                        45);


        PdfWriter writer =
                PdfWriter.getInstance(
                        document,
                        new FileOutputStream(filePath));
        
        writer.setPageEvent(new PageNumberEvent());


        document.open();
        
        
        
        unicodeNormalFont = createUnicodeFont(9, Font.NORMAL);
        unicodeBoldFont = createUnicodeFont(9, Font.BOLD);



        
     // English fonts
        Font universityFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA, 15, Font.BOLD);

        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA, 13, Font.BOLD);

        Font sectionFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA, 11, Font.BOLD);

        Font normalFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA, 9, Font.NORMAL);

        Font smallFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA, 8, Font.NORMAL);

//        // Hindi font for Devanagari text
//        Font hindiFont = createUnicodeFont(9, Font.NORMAL);
//        


        // =====================================================
        // HEADER
        // =====================================================
   int progressreportId = progressReport.getId();
        
        
        String[] approvalDetails = getApprovalDetails(progressreportId);

        String approvalLabel = approvalDetails[0];
        String approverName = approvalDetails[1];

        

        addHeader(
                document,
                universityFont,
                titleFont,
                approvalLabel,
                approverName
        );
        


        // =====================================================
        // REPORT / SCHOLAR INFORMATION
        // =====================================================

        addScholarInformation(
                document,
                progressReport,
                sectionFont,
                normalFont);


        // =====================================================
        // SEMESTER / ATTENDANCE
        // =====================================================

        addSemesterInformation(
                document,
                progressReport,
                sectionFont,
                normalFont);


        // =====================================================
        // PROGRESS REPORT
        // =====================================================

        addProgressReportDetails(
                document,
                progressReport,
                sectionFont,
                normalFont);


//        Integer reportId =
//                progressReport.getId();


        // =====================================================
        // PROGRESS WORK
        // =====================================================

        addProgressWork(
                document,
                progressreportId,
                sectionFont,
                normalFont);


        // =====================================================
        // PUBLICATIONS
        // =====================================================

        addPublications(
                document,
                progressreportId  ,
                sectionFont,
                smallFont);


        // =====================================================
        // CONFERENCES
        // =====================================================

        addConferences(
                document,
                progressreportId,
                sectionFont,
                normalFont);


        // =====================================================
        // VISITS
        // =====================================================

        addVisits(
                document,
                progressreportId,
                sectionFont,
                normalFont);


        // =====================================================
        // ACHIEVEMENTS
        // =====================================================

        addAchievements(
                document,
                progressreportId,
                sectionFont,
                normalFont);


        // =====================================================
        // WORKFLOW / APPROVAL HISTORY
        // =====================================================

        addReviewHistory(
                document,
                progressreportId,
                sectionFont,
                smallFont);


        // =====================================================
        // PDF INFORMATION
        // =====================================================

        addPdfInformation(
                document,
                progressReport,
                versionNo,
                action,
                generatedBy,
                sectionFont,
                normalFont);
     // =====================================================
     // SIGNATURE SECTION
     // =====================================================

        Font signatureFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA,
                        9);
        
     addSignatureSection(
             document,
             progressReport,
             sectionFont,
             signatureFont);

        // =====================================================
        // FOOTER
        // =====================================================

//        addFooter(
//                document,
//                writer,
//                reportId,
//                versionNo);


        document.close();
    }


    // =========================================================
    // HEADER
    // =========================================================

//    private void addHeader(
//            Document document,
//            Font universityFont,
//            Font titleFont)
//            throws DocumentException {
//
//        Paragraph university =
//                new Paragraph(
//                        "DAYALBAGH EDUCATIONAL INSTITUTE",
//                        universityFont);
//
//        university.setAlignment(
//                Element.ALIGN_CENTER);
//
//        document.add(university);
//
//
//        Paragraph title =
//                new Paragraph(
//                        "PH.D. PROGRESS REPORT",
//                        titleFont);
//
//        title.setAlignment(
//                Element.ALIGN_CENTER);
//
//        document.add(title);
//
//
//        Paragraph subtitle =
//                new Paragraph(
//                        "Progress Report Snapshot",
//                        FontFactory.getFont(
//                                FontFactory.HELVETICA,
//                                9));
//
//        subtitle.setAlignment(
//                Element.ALIGN_CENTER);
//
//        document.add(subtitle);
//
//
//        document.add(
//                Chunk.NEWLINE);
//    }
//


private void addHeader(
        Document document,
        Font universityFont,
        Font titleFont,
        String approvalLabel,
        String approverName)
        throws DocumentException {

    Paragraph university = new Paragraph(
            "DAYALBAGH EDUCATIONAL INSTITUTE",
            universityFont);

    university.setAlignment(Element.ALIGN_CENTER);
    university.setSpacingAfter(5);
    document.add(university);

    Paragraph title = new Paragraph(
            "PH.D. PROGRESS REPORT",
            titleFont);

    title.setAlignment(Element.ALIGN_CENTER);
    title.setSpacingAfter(8);
    document.add(title);

    // Approval status
    Font statusFont = FontFactory.getFont(
            FontFactory.HELVETICA,
            11,
            Font.BOLD);

    Paragraph status = new Paragraph(
            approvalLabel, statusFont);

    status.setAlignment(Element.ALIGN_CENTER);
    status.setSpacingAfter(4);
    document.add(status);

    // Approver's name, when applicable
    if (approverName != null && !approverName.trim().isEmpty()) {
        Paragraph approver = new Paragraph(
                "Approved by: " + approverName,
                FontFactory.getFont(
                        FontFactory.HELVETICA, 9));

        approver.setAlignment(Element.ALIGN_CENTER);
        approver.setSpacingAfter(8);
        document.add(approver);
    }

    document.add(Chunk.NEWLINE);
}

    // =========================================================
    // SCHOLAR INFORMATION
    // =========================================================

    private void addScholarInformation(
            Document document,
            ProgressReport progressReport,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "1. SCHOLAR INFORMATION",
                headingFont);


        ScholarSemester ss =
                progressReport
                        .getScholarSemester();
       


        if (ss == null) {

            addText(
                    document,
                    "Scholar semester information is not available.",
                    normalFont);

            return;
        }


        Scholars scholar =
                ss.getScholar();


        if (scholar == null) {

            addText(
                    document,
                    "Scholar information is not available.",
                    normalFont);

            return;
        }

        scholar =
                scholarsRepository
                        .findByIdWithSupervisors(
                                scholar.getScholarId())
                        .orElse(scholar);

        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[] {
                        35f,
                        65f
                });


        addLabelValue(
                table,
                "Scholar Name",
                scholar.getFullName());


        addLabelValue(
                table,
                "Registration No.",
                scholar.getRegistrationNo());


        addLabelValue(
                table,
                "Enrolment No.",
                scholar.getEnrolmentno());


        addLabelValue(
                table,
                "Application No.",
                scholar.getApplicationNumber());


        addLabelValue(
                table,
                "Admission Session",
                scholar.getAdmissionSession());


        addLabelValue(
                table,
                "Admission Date",
                formatDate(
                        scholar.getAdmissionDate()));


        addLabelValue(
                table,
                "Department",
                scholar.getDepartment() == null
                        ? ""
                        : String.valueOf(
                            scholar.getDepartment().getDepartmentName()));


        addLabelValue(
                table,
                "Program",
                scholar.getProgram() == null
                        ? ""
                        : String.valueOf(
                            scholar.getProgram().getProgramname()));


        addLabelValue(
                table,
                "Subject",
                scholar.getSubject());


        addLabelValue(
                table,
                "Mode",
                scholar.getMode());


        addLabelValue(
                table,
                "Research Topic",
                scholar.getResearchTopicEng());


        addLabelValue(
                table,
                "Thesis Title",
                scholar.getThesisTitle());
        
        


        addLabelValue(
                table,
                "Scholar Email",
                scholar.getEmail());


        addLabelValue(
                table,
                "Phone",
                scholar.getPhone());


        // Supervisor information
        String supervisors =
                getSupervisorNames(scholar);


        addLabelValue(
                table,
                "Supervisor(s)",
                supervisors);


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // SUPERVISORS
    // =========================================================

    private String getSupervisorNames(
            Scholars scholar) {

        StringBuilder result =
                new StringBuilder();


        List<ScholarSupervisor> supervisors =
                scholar.getSupervisors();


        if (supervisors != null) {

            for (ScholarSupervisor ss :
                    supervisors) {

                if (ss == null) {
                    continue;
                }


                if (ss.getSupervisor() != null) {

                    if (result.length() > 0) {
                        result.append("\n");
                    }


                    result.append(
                            ss.getSupervisor()
                              .getName());


                    if (ss.getRole() != null) {

                        result.append(
                                " (");

                        result.append(
                                ss.getRole());

                        result.append(
                                ")");
                    }
                }
            }
        }


        // Fallback
        if (result.length() == 0) {

            if (scholar.getSupervisorId() != null) {

                result.append(
                        "Supervisor ID: ");

                result.append(
                        scholar.getSupervisorId());
            }


            if (scholar.getCoSupervisorId() != null) {

                if (result.length() > 0) {
                    result.append("\n");
                }

                result.append(
                        "Co-Supervisor ID: ");

                result.append(
                        scholar.getCoSupervisorId());
            }
        }


        return result.toString();
    }


    // =========================================================
    // SEMESTER INFORMATION
    // =========================================================

    private void addSemesterInformation(
            Document document,
            ProgressReport progressReport,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "2. SEMESTER / ATTENDANCE",
                headingFont);


        ScholarSemester ss =
                progressReport
                        .getScholarSemester();


        if (ss == null) {

            addText(
                    document,
                    "Semester information is not available.",
                    normalFont);

            return;
        }


        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);


        String semesterName = "";

        if (ss.getSemester() != null) {
            semesterName =
                    String.valueOf(
                            ss.getSemester().getSemesterName());
        }


        addLabelValue(
                table,
                "Semester",
                semesterName);


        addLabelValue(
                table,
                "Registration Date",
                formatDate(
                        ss.getRegistrationDate()));


        addLabelValue(
                table,
                "Total Sessions",
                formatInteger(
                        ss.getTotalsessions()));


        addLabelValue(
                table,
                "Attended Sessions",
                formatInteger(
                        ss.getAttendedsessions()));


        addLabelValue(
                table,
                "Attendance %",
                ss.getAttendancePercentage()
                        == null
                        ? ""
                        : String.valueOf(
                            ss.getAttendancePercentage()));


        addLabelValue(
                table,
                "Attendance Remarks",
                ss.getAttendanceremarks());


        addLabelValue(
                table,
                "Overall Remarks",
                ss.getOverallRemarks());


        addLabelValue(
                table,
                "Review Status",
                ss.getReviewStatus()
                        == null
                        ? ""
                        : ss.getReviewStatus()
                            .toString());


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // PROGRESS REPORT
    // =========================================================

    private void addProgressReportDetails(
            Document document,
            ProgressReport progressReport,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "3. PROGRESS REPORT DETAILS",
                headingFont);


        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);


        addLabelValue(
                table,
                "Report ID",
                formatInteger(
                        progressReport.getId()));


        addLabelValue(
                table,
                "Reporting Period",
                formatDate(
                        progressReport.getPeriodStart())
                        + " to "
                        + formatDate(
                            progressReport.getPeriodEnd()));


        addLabelValue(
                table,
                "Progress Status",
                progressReport
                        .getProgressStatus()
                        == null
                        ? ""
                        : progressReport
                            .getProgressStatus()
                            .toString());


        addLabelValue(
                table,
                "Research Work",
                progressReport.getResearchWork());


        addLabelValue(
                table,
                "Summary",
                progressReport.getSummary());


        addLabelValue(
                table,
                "Committee State",
                progressReport
                        .getCommitteeState()
                        == null
                        ? ""
                        : String.valueOf(
                            progressReport
                                .getCommitteeState()));


        addLabelValue(
                table,
                "Meeting Date",
                formatDate(
                        progressReport
                            .getMeetingDate()));


        addLabelValue(
                table,
                "Next Actions",
                progressReport.getNextActions());


        addLabelValue(
                table,
                "Submitted At",
                formatDateTime(
                        progressReport
                            .getSubmittedAt()));


        Report report =
                progressReport.getReport();


        if (report != null) {

            addLabelValue(
                    table,
                    "Workflow Status",
                    report.getStatus()
                            == null
                            ? ""
                            : report.getStatus()
                                .toString());


            addLabelValue(
                    table,
                    "Current Sequence No.",
                    formatInteger(
                            report
                                .getCurrentSequenceNo()));


            addLabelValue(
                    table,
                    "Workflow Submitted On",
                    formatDateTime(
                            report
                                .getSubmittedOn()));
        }


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // PROGRESS WORK
    // =========================================================

    private void addProgressWork(
            Document document,
            Integer reportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "4. RESEARCH / PROGRESS WORK",
                headingFont);


        List<ProgressWork> works =
                progressWorkRepository
                        .findByReportId(reportId);


        if (works == null ||
            works.isEmpty()) {

            addText(
                    document,
                    "No progress work recorded.",
                    normalFont);

            return;
        }


        PdfPTable table =
                new PdfPTable(3);

        table.setWidthPercentage(100);


        addHeaderCell(
                table,
                "Stage");


        addHeaderCell(
                table,
                "Objective No.");


        addHeaderCell(
                table,
                "Completion %");


        for (ProgressWork work :
                works) {

            addCell(
                    table,
                    work.getStage());


            addCell(
                    table,
                    work.getObjectiveNo());


            addCell(
                    table,
                    formatInteger(
                            work
                                .getCompletionPercentage()));
        }


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // PUBLICATIONS
    // =========================================================

    private void addPublications(
            Document document,
            Integer reportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "5. PUBLICATIONS",
                headingFont);


        List<Publication> list =
                publicationRepository
                        .findByReportId(reportId);


        if (list == null ||
            list.isEmpty()) {

            addText(
                    document,
                    "No publications recorded.",
                    normalFont);

            return;
        }


        /*
         * Publications contain many columns.
         * Landscape-like compact table is handled
         * by smaller font / wrapping.
         */

        PdfPTable table =
                new PdfPTable(8);

        table.setWidthPercentage(100);


        addHeaderCell(
                table,
                "Authors");

        addHeaderCell(
                table,
                "Title");

        addHeaderCell(
                table,
                "Journal");

        addHeaderCell(
                table,
                "Volume");

        addHeaderCell(
                table,
                "Page No.");

        addHeaderCell(
                table,
                "Year");

        addHeaderCell(
                table,
                "Impact Factor");

        addHeaderCell(
                table,
                "Indexing");


        for (Publication p : list) {

            addCell(
                    table,
                    p.getAuthors());

            addCell(
                    table,
                    p.getTitle());

            addCell(
                    table,
                    p.getJournal());

            addCell(
                    table,
                    p.getVolume());

            addCell(
                    table,
                    p.getPageNo());

            addCell(
                    table,
                    p.getYear() == null
                            ? ""
                            : String.valueOf(
                                p.getYear()));

            addCell(
                    table,
                    p.getImpact());

            addCell(
                    table,
                    p.getIndexing());
        }


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // CONFERENCES
    // =========================================================

    private void addConferences(
            Document document,
            Integer reportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "6. CONFERENCES / SEMINARS",
                headingFont);


        List<ConferenceAttended> list =
                conferenceRepository
                        .findByReportId(reportId);


        if (list == null ||
            list.isEmpty()) {

            addText(
                    document,
                    "No conferences recorded.",
                    normalFont);

            return;
        }


        for (ConferenceAttended c :
                list) {

            PdfPTable table =
                    new PdfPTable(2);

            table.setWidthPercentage(100);


            addLabelValue(
                    table,
                    "Authors",
                    c.getAuthors());

            addLabelValue(
                    table,
                    "Title",
                    c.getTitle());

            addLabelValue(
                    table,
                    "Type",
                    c.getType());

            addLabelValue(
                    table,
                    "Level",
                    c.getLevel());

            addLabelValue(
                    table,
                    "Organizer",
                    c.getOrganizer());

            addLabelValue(
                    table,
                    "Place",
                    c.getPlace());

            addLabelValue(
                    table,
                    "Dates",
                    c.getDates());

            addLabelValue(
                    table,
                    "Presentation Type",
                    c.getPresentationType());

            addLabelValue(
                    table,
                    "Participation",
                    c.getParticipation());

            addLabelValue(
                    table,
                    "Funding",
                    c.getFunding());


            document.add(table);

            document.add(
                    Chunk.NEWLINE);
        }
    }


    // =========================================================
    // VISITS
    // =========================================================

    private void addVisits(
            Document document,
            Integer reportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "7. VISITS",
                headingFont);


        List<Visit> list =
                visitRepository
                        .findByReportId(reportId);


        if (list == null ||
            list.isEmpty()) {

            addText(
                    document,
                    "No visits recorded.",
                    normalFont);

            return;
        }


        for (Visit v : list) {

            PdfPTable table =
                    new PdfPTable(2);

            table.setWidthPercentage(100);


            addLabelValue(
                    table,
                    "Institute",
                    v.getInstitute());

            addLabelValue(
                    table,
                    "Contact Person",
                    v.getContactPerson());

            addLabelValue(
                    table,
                    "Designation",
                    v.getDesignation());

            addLabelValue(
                    table,
                    "Place",
                    v.getPlace());

            addLabelValue(
                    table,
                    "Dates",
                    v.getDates());

            addLabelValue(
                    table,
                    "Year",
                    v.getYear() == null
                            ? ""
                            : String.valueOf(
                                v.getYear()));

            addLabelValue(
                    table,
                    "Purpose",
                    v.getPurpose());


            document.add(table);

            document.add(
                    Chunk.NEWLINE);
        }
    }


    // =========================================================
    // ACHIEVEMENTS
    // =========================================================

    private void addAchievements(
            Document document,
            Integer reportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "8. ACHIEVEMENTS / TEACHING",
                headingFont);


        List<Achievement> list =
                achievementRepository
                        .findByReportId(reportId);


        if (list == null ||
            list.isEmpty()) {

            addText(
                    document,
                    "No achievements recorded.",
                    normalFont);

            return;
        }


        PdfPTable table =
                new PdfPTable(4);

        table.setWidthPercentage(100);


        addHeaderCell(
                table,
                "Awards");

        addHeaderCell(
                table,
                "Patents");

        addHeaderCell(
                table,
                "Teaching Hours");

        addHeaderCell(
                table,
                "Teaching Type");


        for (Achievement a :
                list) {

            addCell(
                    table,
                    a.getAwards());

            addCell(
                    table,
                    a.getPatents());

            addCell(
                    table,
                    a.getTeachingHours()
                            == null
                            ? ""
                            : String.valueOf(
                                a.getTeachingHours()));

            addCell(
                    table,
                    a.getTeachingType());
        }


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // REVIEW HISTORY
    // =========================================================

    private void addReviewHistory(
            Document document,
            Integer progressreportId,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "9. REVIEW / APPROVAL HISTORY",
                headingFont);
        
            int reportId  = reportRepository.findByProgressReportId(progressreportId)
            		.orElseThrow(()->new RuntimeException(
                            "Progress report not found: " + progressreportId))
                        .getId();
           
            List<ReviewHistory> history =
                historyRepository
                        .findByReportIdOrderByActedAtAsc(
                                reportId);


        if (history == null ||
            history.isEmpty()) {

            addText(
                    document,
                    "No review history available.",
                    normalFont);

            return;
        }


        PdfPTable table =
                new PdfPTable(5);

        table.setWidthPercentage(100);


        addHeaderCell(
                table,
                "Role");

        addHeaderCell(
                table,
                "Action");

        addHeaderCell(
                table,
                "Acted By");

        addHeaderCell(
                table,
                "Date / Time");

        addHeaderCell(
                table,
                "Remarks");


        for (ReviewHistory h :
                history) {


            String roleName =
                    getRoleName(
                            h.getRoleId());


            addCell(
                    table,
                    roleName);


            addCell(
                    table,
                    h.getAction());

           
           User user = userRepository.findById(h.getActedBy())
        		    .orElseThrow(() -> new RuntimeException(
        		        "User not found with ID: " + h.getActedBy()
        		    ));
           String actedby = user.getName();
            addCell(
                    table,
                    h.getActedBy()
                            == null
                            ? ""
                            : actedby);


            addCell(
                    table,
                    formatDateTime(
                            h.getActedAt()));


            addCell(
                    table,
                    h.getRemarks());
        }


        document.add(table);

        document.add(
                Chunk.NEWLINE);
    }


    // =========================================================
    // ROLE NAME
    // =========================================================

    private String getRoleName(
            Integer roleId) {

        if (roleId == null) {
            return "";
        }


        Optional<Role> role =
                roleRepository.findById(
                        roleId);


        if (role.isPresent()) {

            return role.get()
                       .getName()
                       .toString();
        }


        return "ROLE ID: " + roleId;
    }


    // =========================================================
    // PDF INFORMATION
    // =========================================================

    private void addPdfInformation(
            Document document,
            ProgressReport progressReport,
            Integer versionNo,
            String action,
            Integer generatedBy,
            Font headingFont,
            Font normalFont)
            throws DocumentException {


        addSectionHeading(
                document,
                "10. PDF SNAPSHOT INFORMATION",
                headingFont);


        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);


        addLabelValue(
                table,
                "Report ID",
                formatInteger(
                        progressReport.getId()));


        addLabelValue(
                table,
                "PDF Version",
                formatInteger(
                        versionNo));


        addLabelValue(
                table,
                "Action",
                action);


//        addLabelValue(
//                table,
//                "Generated By",
//                generatedBy == null
//                        ? ""
//                        : String.valueOf(
//                            generatedBy));

        String generatedByName = "";

        if (generatedBy != null) {
            generatedByName = userRepository.findById(generatedBy)
                    .map(user -> user.getName()) // Use your actual name getter
                    .orElse("Unknown User");
        }

        addLabelValue(
                table,
                "Generated By",
                generatedByName);
        
        
        addLabelValue(
                table,
                "Generated At",
                formatDateTime(
                        LocalDateTime.now()));


        document.add(table);
    }


    // =========================================================
    // FOOTER
    // =========================================================

    private void addFooter(
            Document document,
            PdfWriter writer,
            Integer reportId,
            Integer versionNo) {

        /*
         * This method intentionally keeps footer creation
         * simple. For true page numbers, use a PdfPageEvent.
         */
    }


    // =========================================================
    // SECTION HEADING
    // =========================================================

    private void addSectionHeading(
            Document document,
            String text,
            Font font)
            throws DocumentException {

        Paragraph p =
                new Paragraph(
                        text,
                        font);

        p.setSpacingBefore(8);
        p.setSpacingAfter(6);

        document.add(p);
    }


    // =========================================================
    // TEXT
    // =========================================================

//    private void addText(
//            Document document,
//            String text,
//            Font font)
//            throws DocumentException {
//
//        document.add(
//                new Paragraph(
//                        safe(text),
//                        font));
//
//        document.add(
//                Chunk.NEWLINE);
//    }
    
    private void addText(
            Document document,
            String text,
            Font font)
            throws DocumentException {

        document.add(
                new Paragraph(
                        safe(text),
                        font));

        document.add(Chunk.NEWLINE);
    }


    // =========================================================
    // LABEL / VALUE
    // =========================================================

//    private void addLabelValue(
//            PdfPTable table,
//            String label,
//            String value) {
//
//        PdfPCell labelCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(label)));
//
//        labelCell.setPadding(5);
//
//
//        PdfPCell valueCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value)));
//
//        valueCell.setPadding(5);
//
//
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
    
    
//    private void addLabelValue(
//            PdfPTable table,
//            String label,
//            String value) {
//
//        PdfPCell labelCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(label),
//                                unicodeNormalFont));
//
//        labelCell.setPadding(5);
//
//        PdfPCell valueCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value),
//                                unicodeNormalFont));
//
//        valueCell.setPadding(5);
//
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
    
//    private void addLabelValue(
//            PdfPTable table,
//            String label,
//            String value) {
//
//        PdfPCell labelCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(label)));
//
//        labelCell.setPadding(5);
//
//        PdfPCell valueCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value)));
//
//        valueCell.setPadding(5);
//
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
    
//    private void addLabelValue(
//            PdfPTable table,
//            String label,
//            String value) {
//
//        PdfPCell labelCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(label),
//                                unicodeNormalFont));
//
//        labelCell.setPadding(5);
//
//        PdfPCell valueCell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value),
//                                unicodeNormalFont));
//
//        valueCell.setPadding(5);
//
//        table.addCell(labelCell);
//        table.addCell(valueCell);
//    }
    
    private void addLabelValue(
            PdfPTable table,
            String label,
            String value) {

        Font labelFont;
        Font valueFont;

        try {
            labelFont = getFontForText(label, 9, Font.NORMAL);
            valueFont = getFontForText(value, 9, Font.NORMAL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(
                                safe(label),
                                labelFont));

        labelCell.setPadding(5);

        PdfPCell valueCell =
                new PdfPCell(
                        new Phrase(
                                safe(value),
                                valueFont));

        valueCell.setPadding(5);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // =========================================================
    // TABLE HEADER
    // =========================================================

//    private void addHeaderCell(
//            PdfPTable table,
//            String value) {
//
//        PdfPCell cell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value)));
//
//        cell.setPadding(5);
//
//        cell.setHorizontalAlignment(
//                Element.ALIGN_CENTER);
//
//        table.addCell(cell);
//    }
    
//    private void addHeaderCell(
//            PdfPTable table,
//            String value) {
//
//        PdfPCell cell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value),
//                                unicodeBoldFont));
//
//        cell.setPadding(5);
//
//        cell.setHorizontalAlignment(
//                Element.ALIGN_CENTER);
//
//        table.addCell(cell);
//    }

    private void addHeaderCell(
            PdfPTable table,
            String value) {

        Font font =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        8);

        PdfPCell cell =
                new PdfPCell(
                        new Phrase(
                                safe(value),
                                font));

        cell.setPadding(5);

        cell.setHorizontalAlignment(
                Element.ALIGN_CENTER);

        table.addCell(cell);
    }

    // =========================================================
    // TABLE CELL
    // =========================================================

//    private void addCell(
//            PdfPTable table,
//            String value) {
//
//        PdfPCell cell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value)));
//
//        cell.setPadding(4);
//
//        table.addCell(cell);
//    }

//    private void addCell(
//            PdfPTable table,
//            String value) {
//
//        PdfPCell cell =
//                new PdfPCell(
//                        new Phrase(
//                                safe(value),
//                                unicodeNormalFont));
//
//        cell.setPadding(4);
//
//        table.addCell(cell);
//    }

    
    private void addCell(
            PdfPTable table,
            String value) {

        try {

            Font font =
                    getFontForText(
                            value,
                            8,
                            Font.NORMAL);

            PdfPCell cell =
                    new PdfPCell(
                            new Phrase(
                                    safe(value),
                                    font));

            cell.setPadding(4);

            table.addCell(cell);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value;
    }


    // =========================================================
    // FORMATTING
    // =========================================================

    private String formatDate(
            java.time.LocalDate date) {

        return date == null
                ? ""
                : date.toString();
    }


    private String formatDateTime(
            LocalDateTime dateTime) {

        return dateTime == null
                ? ""
                : dateTime.toString()
                        .replace("T", " ");
    }


    private String formatInteger(
            Integer value) {

        return value == null
                ? ""
                : String.valueOf(value);
    }
    
    private Font createUnicodeFont(float size, int style) throws Exception {

        String fontPath = "fonts/NotoSansDevanagari-Regular.ttf";

        BaseFont baseFont = BaseFont.createFont(
                fontPath,
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );

        return new Font(baseFont, size, style);
    }
    
    private Font getFontForText(String text, float size, int style)
            throws Exception {

        if (text != null && text.matches(".*[\\u0900-\\u097F].*")) {

            return createUnicodeFont(size, style);

        } else {

            return FontFactory.getFont(
                    style == Font.BOLD
                            ? FontFactory.HELVETICA_BOLD
                            : FontFactory.HELVETICA,
                    size);
        }
    }
    private void addSignatureSection(
            Document document,
            ProgressReport progressReport,
            Font headingFont,
            Font normalFont)
            throws DocumentException {

        addSectionHeading(
                document,
                "11. SIGNATURE / APPROVAL",
                headingFont);

        PdfPTable table =
                new PdfPTable(2);

        table.setWidthPercentage(100);

        table.setWidths(
                new float[] { 50f, 50f });

        // -------------------------------------------------
        // Row 1
        // -------------------------------------------------

        addSignatureCell(
                table,
                "Supervisor",
                getSupervisorName(progressReport),
                normalFont);

        addSignatureCell(
                table,
                "Co-Supervisor",
                getCoSupervisorName(progressReport),
                normalFont);

        // -------------------------------------------------
        // Row 2
        // -------------------------------------------------
        int depid=progressReport.getScholarSemester().getDepartmentId();
        int facid=progressReport.getScholarSemester().getFacultyId();
        DepartmentRoleAssignment assignment =
        	    departmentRoleAssignmentRepository.findByDepartment_DepartmentIdAndRole(depid,"ROLE_HOD")
        	        .orElseThrow(() ->
        	            new RuntimeException(
        	                "Department role assignment not found for department: "
        	                + depid
        	            )
        	        );
        String hod =assignment.getUser().getName();
        
        addSignatureCell(
                table,
                "Head of Department (HOD)",
                hod,
                normalFont);
        
        FacultyRoleAssignment facassignment =
        	    facultyRoleRepository.findByFaculty_IdAndRole(facid,"ROLE_DEAN")
        	        .orElseThrow(() ->
        	            new RuntimeException(
        	                "Faculity role assignment not found for faculity: "
        	                + facid
        	            )
        	        );
        
        String dean =facassignment.getUser().getName();
        addSignatureCell(
                table,
                "Dean",
                dean,
                normalFont);

        document.add(table);

        document.add(Chunk.NEWLINE);

        // -------------------------------------------------
        // PG Dean
        // Final Authority
        // -------------------------------------------------

        PdfPTable pgDeanTable =
                new PdfPTable(1);

        pgDeanTable.setWidthPercentage(50);

        pgDeanTable.setHorizontalAlignment(
                Element.ALIGN_CENTER);
        
        List<User> pgDeanList =userRepository.findUsersByRole(ERole.ROLE_PG_DEAN);
       
        String pgDeanName = "";

        if (pgDeanList != null && !pgDeanList.isEmpty()) {
            pgDeanName = pgDeanList.get(0).getName();
        }
        
        

        addSignatureCell(
                pgDeanTable,
                "PG Dean",
                pgDeanName,
                normalFont);

        document.add(pgDeanTable);
    }
    
    private void addSignatureCell(
            PdfPTable table,
            String designation,
            String name,
            Font font) {

        PdfPCell cell =
                new PdfPCell();

        cell.setPadding(8);

        // Space for physical signature
        cell.setMinimumHeight(110);

        Paragraph signature =
                new Paragraph(
                        "\n\n\nSignature: ____________________",
                        font);

        cell.addElement(signature);

        Paragraph designationParagraph =
                new Paragraph(
                        designation,
                        font);

        cell.addElement(
                designationParagraph);

        if (name != null &&
            !name.trim().isEmpty()) {

            cell.addElement(
                    new Paragraph(
                            "Name: " + name,
                            font));

        } else {

            cell.addElement(
                    new Paragraph(
                            "Name: ____________________",
                            font));
        }

        cell.addElement(
                new Paragraph(
                        "Date: ____________________",
                        font));

        table.addCell(cell);
    }
    
//    private String getSupervisorName(ProgressReport progressReport) {
//
//        if (progressReport.getScholarSemester() == null ||
//            progressReport.getScholarSemester().getScholar() == null) {
//            return "";
//        }
//
//        Scholars scholar = progressReport.getScholarSemester().getScholar();
//
//        scholar = scholarsRepository
//                .findByIdWithSupervisors(scholar.getScholarId())
//                .orElse(scholar);
//
//        if (scholar.getSupervisors() != null) {
//            for (ScholarSupervisor ss : scholar.getSupervisors()) {
//
//                if (ss != null &&
//                    ss.getSupervisor() != null &&
//                    ss.getRole() != null &&
//                    ss.getRole().toString().equalsIgnoreCase("SUPERVISOR")) {
//
//                    return ss.getSupervisor().getName();
//                }
//            }
//        }
//
//        return "";
//    }
//    private String getCoSupervisorName(ProgressReport progressReport) {
//
//        if (progressReport.getScholarSemester() == null ||
//            progressReport.getScholarSemester().getScholar() == null) {
//            return "";
//        }
//
//        Scholars scholar = progressReport.getScholarSemester().getScholar();
//
//        scholar = scholarsRepository
//                .findByIdWithSupervisors(scholar.getScholarId())
//                .orElse(scholar);
//
//        if (scholar.getSupervisors() != null) {
//            for (ScholarSupervisor ss : scholar.getSupervisors()) {
//
//                if (ss != null &&
//                    ss.getSupervisor() != null &&
//                    ss.getRole() != null &&
//                    ss.getRole().toString().equalsIgnoreCase("CO_SUPERVISOR")) {
//
//                    return ss.getSupervisor().getName();
//                }
//            }
//        }
//
//        return "";
//    }

    

//private static class PageNumberEvent
//extends PdfPageEventHelper {
//
//private PdfTemplate totalPages;
//private BaseFont font;
//
//@Override
//public void onOpenDocument(
//    PdfWriter writer,
//    Document document) {
//
//totalPages =
//        writer.getDirectContent()
//                .createTemplate(50, 20);
//
//try {
//    font = BaseFont.createFont(
//            BaseFont.HELVETICA,
//            BaseFont.WINANSI,
//            BaseFont.NOT_EMBEDDED);
//
//} catch (Exception e) {
//    throw new RuntimeException(e);
//}
//}
//
////@Override
////public void onEndPage(
////    PdfWriter writer,
////    Document document) {
////
////PdfPTable footer =
////        new PdfPTable(1);
////
////try {
////
////    footer.setTotalWidth(
////            document.right()
////            - document.left());
////
////    footer.setWidths(
////            new float[] { 1 });
////
////    footer.setLockedWidth(true);
////
////    PdfPCell cell =
////            new PdfPCell();
////
////    cell.setBorder(
////            Rectangle.NO_BORDER);
////
////    cell.setHorizontalAlignment(
////            Element.ALIGN_CENTER);
////
////    Phrase phrase =
////            new Phrase();
////
////    phrase.add(
////            new Chunk(
////                    "Page "
////                    + writer.getPageNumber()
////                    + " of ",
////                    new Font(font, 8)));
////
////    phrase.add(
////            new Chunk(
////                    new Chunk(
////                            totalPages,
////                            0,
////                            0),
////                    new Font(font, 8)));
////
////    cell.addElement(phrase);
////
////    footer.addCell(cell);
////
////    footer.writeSelectedRows(
////            0,
////            -1,
////            document.left(),
////            document.bottom() - 10,
////            writer.getDirectContent());
////
////} catch (Exception e) {
////    throw new RuntimeException(e);
////}
////}
//
//@Override
//public void onEndPage(
//        PdfWriter writer,
//        Document document) {
//
//    PdfContentByte cb =
//            writer.getDirectContent();
//
//    Phrase footer =
//            new Phrase(
//                    "Page "
//                    + writer.getPageNumber()
//                    + " of ",
//                    new Font(font, 8));
//
//    ColumnText.showTextAligned(
//            cb,
//            Element.ALIGN_CENTER,
//            footer,
//            (document.left() + document.right()) / 2,
//            document.bottom() - 20,
//            0);
//
//    cb.addTemplate(
//            totalPages,
//            (document.right() + document.left()) / 2 + 20,
//            document.bottom() - 20);
//}
//
////@Override
////public void onCloseDocument(
////    PdfWriter writer,
////    Document document) {
////
////totalPages.beginText();
////
////totalPages.setFontAndSize(
////        font,
////        8);
////
////totalPages.setTextMatrix(
////        0,
////        0);
////
////totalPages.showText(
////        String.valueOf(
////                writer.getPageNumber()));
////
////totalPages.endText();
////}
//
//@Override
//public void onCloseDocument(
//        PdfWriter writer,
//        Document document) {
//
//    totalPages.beginText();
//
//    totalPages.setFontAndSize(
//            font,
//            8);
//
//    totalPages.setTextMatrix(
//            0,
//            0);
//
//    totalPages.showText(
//            String.valueOf(
//                    writer.getPageNumber() - 1));
//
//    totalPages.endText();
//}
//}
//
    private static class PageNumberEvent extends PdfPageEventHelper {

        private PdfTemplate totalPages;
        private BaseFont font;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            totalPages = writer.getDirectContent().createTemplate(40, 12);

            try {
                font = BaseFont.createFont(
                        BaseFont.HELVETICA,
                        BaseFont.WINANSI,
                        BaseFont.NOT_EMBEDDED);
            } catch (Exception e) {
                throw new RuntimeException("Unable to create page-number font", e);
            }
        }

//        @Override
//        public void onEndPage(PdfWriter writer, Document document) {
//            PdfContentByte canvas = writer.getDirectContent();
//
//            float centerX = (document.left() + document.right()) / 2;
//            float y = document.bottom() - 20;
//
//            Phrase phrase = new Phrase(
//                    "Page " + writer.getPageNumber() + " of ",
//                    new Font(font, 8));
//
//            ColumnText.showTextAligned(
//                    canvas,
//                    Element.ALIGN_CENTER,
//                    phrase,
//                    centerX - 10,
//                    y,
//                    0);
//
//            canvas.addTemplate(totalPages, centerX + 14, y - 2);
//        }
        
        @Override
        public void onEndPage(
                PdfWriter writer,
                Document document) {

            PdfContentByte canvas = writer.getDirectContent();

            float centerX =
                    (document.left() + document.right()) / 2;

            float y = document.bottom() - 20;

            Font footerFont = new Font(font, 8);

            // Draw "Page X of" with right alignment
            Phrase phrase = new Phrase(
                    "Page " + writer.getPageNumber() + " of",
                    footerFont);

            ColumnText.showTextAligned(
                    canvas,
                    Element.ALIGN_RIGHT,
                    phrase,
                    centerX + 5,
                    y,
                    0);

            // Draw total page count on the same baseline
            canvas.addTemplate(
                    totalPages,
                    centerX + 7,
                    y - 2);
        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            totalPages.beginText();
            totalPages.setFontAndSize(font, 8);
            totalPages.setTextMatrix(0, 0);
            totalPages.showText(String.valueOf(writer.getPageNumber() - 1));
            totalPages.endText();
        }
    }
private String getSupervisorName(ProgressReport progressReport) {

    if (progressReport.getScholarSemester() == null ||
        progressReport.getScholarSemester().getScholar() == null) {
        return "";
    }

    Scholars scholar = progressReport.getScholarSemester().getScholar();

    scholar = scholarsRepository
    		
            .findByIdWithSupervisors(scholar.getScholarId())
            .orElse(scholar);

    if (scholar.getSupervisors() != null) {
        for (ScholarSupervisor ss : scholar.getSupervisors()) {

            if (ss != null &&
                ss.getSupervisor() != null &&
                ss.getRole() != null &&
                ss.getRole().toString().equalsIgnoreCase("PRIMARY")) {

                return ss.getSupervisor().getName();
            }
        }
    }

    return "";
}
private String getCoSupervisorName(ProgressReport progressReport) {

    if (progressReport.getScholarSemester() == null ||
        progressReport.getScholarSemester().getScholar() == null) {
        return "";
    }

    Scholars scholar = progressReport.getScholarSemester().getScholar();

    scholar = scholarsRepository
            .findByIdWithSupervisors(scholar.getScholarId())
            .orElse(scholar);

    if (scholar.getSupervisors() != null) {
        for (ScholarSupervisor ss : scholar.getSupervisors()) {

            if (ss != null &&
                ss.getSupervisor() != null &&
                ss.getRole() != null &&
                ss.getRole().toString().equalsIgnoreCase("CO_SUPERVISOR")) {

                return ss.getSupervisor().getName() ;
            }
        }
    }

    return "";
}

private void addApprovalHeader(
        Document document,
        Font titleFont,
        Font normalFont,
        String approvalLabel,
        String approverName) throws DocumentException {

    Paragraph institute = new Paragraph(
            "DAYALBAGH EDUCATIONAL INSTITUTE", titleFont);
    institute.setAlignment(Element.ALIGN_CENTER);
    institute.setSpacingAfter(4);
    document.add(institute);

    Paragraph reportTitle = new Paragraph(
            "PhD Progress Report", normalFont);
    reportTitle.setAlignment(Element.ALIGN_CENTER);
    reportTitle.setSpacingAfter(8);
    document.add(reportTitle);

    PdfPTable statusTable = new PdfPTable(1);
    statusTable.setWidthPercentage(100);

    PdfPCell statusCell = new PdfPCell();
    statusCell.setPadding(8);
    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);

    Paragraph status = new Paragraph(approvalLabel, titleFont);
    status.setAlignment(Element.ALIGN_CENTER);
    statusCell.addElement(status);

    if (approverName != null && !approverName.trim().isEmpty()) {
        Paragraph approver = new Paragraph(
                "Approved by: " + approverName, normalFont);
        approver.setAlignment(Element.ALIGN_CENTER);
        statusCell.addElement(approver);
    }

    statusTable.addCell(statusCell);
    document.add(statusTable);
    document.add(new Paragraph(" "));
}


private String[] getApprovalDetails(Integer reportId) {

    List<Object[]> history =
            reviewHistoryRepository.findApprovalDetails(reportId);

    for (Object[] row : history) {

        String roleName = row[0] == null
                ? "" : row[0].toString();

        String action = row[1] == null
                ? "" : row[1].toString();

        String approverName = row[2] == null
                ? "" : row[2].toString();

        if (!"APPROVED".equalsIgnoreCase(action)) {
            continue;
        }

        String approvalLabel;

        if ("ROLE_PG_DEAN".equalsIgnoreCase(roleName)) {
            approvalLabel = "FINAL APPROVAL BY PG DEAN";
        } else if ("ROLE_DEAN".equalsIgnoreCase(roleName)) {
            approvalLabel = "APPROVED BY DEAN - AWAITING FINAL APPROVAL";
        } else {
            approvalLabel = "APPROVED BY "
                    + roleName.replace("ROLE_", "").replace("_", " ");
        }

        return new String[] {
            approvalLabel,
            approverName
        };
    }

    return new String[] {
        "UNDER REVIEW",
        ""
    };
}

public Resource getLatestPdfForDownload(
        Integer reportId, String username) throws IOException {

    // 1. Retrieve all PDF versions for this report
    List<ProgressReportPdf> pdfList =
            pdfRepository.findByReportIdOrderByVersionNoAsc(reportId);

    if (pdfList == null || pdfList.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No PDF found for report ID: " + reportId);
    }

    // 2. The last record has the highest version number
    ProgressReportPdf latestPdf =
            pdfList.get(pdfList.size() - 1);

    // 3. Get the saved PDF file path
    String filePath = latestPdf.getFilePath();

    if (filePath == null || filePath.trim().isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "PDF file path is not available");
    }

    // 4. Check whether the file exists
    Path path = Paths.get(filePath);

    if (!Files.isRegularFile(path) ||
            !Files.isReadable(path)) {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "PDF file does not exist or cannot be read");
    }

    // 5. Return the PDF as a downloadable resource
    return new FileSystemResource(path);
}

}