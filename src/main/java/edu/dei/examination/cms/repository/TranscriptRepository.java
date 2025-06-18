package edu.dei.examination.cms.repository;

import edu.dei.examination.cms.model.Transcript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TranscriptRepository {

    @Autowired
    @Qualifier("cmsJdbcTemplate")
    private JdbcTemplate cmsJdbcTemplate;
    

    // Method to get transcript data
    private final String transcriptQuery = "SELECT srsh.sgpa Sgpa,srsh.roll_number, " +
            "substring(pch.semester_code,3,2) as sem, " +
            "concat(substring(pr.session_start_date,1,4), '-', substring(pr.session_end_date,1,4)) as session, " +
            "concat(sc.course_code, ':', cmps.course_name) as course_code_name, " +
            "sms.final_grade_point, " +
            "cmps.credits as credit " +
            "FROM student_registration_semester_header srsh " +
            "JOIN program_course_header pch ON pch.program_course_key = srsh.program_course_key " +
            "JOIN student_course sc ON sc.program_course_key = srsh.program_course_key " +
            "AND sc.roll_number = srsh.roll_number AND sc.entity_id = srsh.entity_id " +
            "AND srsh.session_start_date = sc.semester_start_date " +
            "AND srsh.session_end_date = sc.semester_end_date " +
            "JOIN student_marks_summary sms ON sms.roll_number = sc.roll_number " +
            "AND sms.program_course_key = sc.program_course_key " +
            "AND sc.course_code = sms.course_code " +
            "AND sc.semester_start_date = sms.semester_start_date " +
            "AND sc.semester_end_date = sms.semester_end_date " +
            "AND sc.entity_id = sms.entity_id " +
            "JOIN program_registration pr ON pr.program_course_key = srsh.program_course_key " +
            "AND srsh.session_start_date = pr.semester_start_date " +
            "AND srsh.session_end_date = pr.semester_end_date " +
            "AND srsh.entity_id = sc.entity_id " +
            "JOIN course_master_per_session cmps ON cmps.course_code = sms.course_code " +
            "AND cmps.session_start_date = pr.session_start_date " +
            "AND cmps.session_end_date = pr.session_end_date " +
            "JOIN student_program sp ON sp.roll_number = srsh.roll_number " +
            "AND sp.program_id = pch.program_id " +
            "WHERE srsh.roll_number = ? AND sp.program_status in ('PAS','SWT') AND srsh.status = 'PAS' " +
            "GROUP BY sc.semester_start_date, sc.course_code ORDER BY sc.semester_start_date, sc.course_code";
   
    public List<Transcript> getTranscript(String rollNumber) {
        return cmsJdbcTemplate.query(transcriptQuery, new Object[]{rollNumber}, new RowMapper<Transcript>() {
            @Override
            public Transcript mapRow(ResultSet rs, int rowNum) throws SQLException {
                Transcript Transcript = new Transcript();
                Transcript.setRoll_number(rs.getString("roll_number"));
                Transcript.setSem(rs.getString("sem"));
                Transcript.setSession(rs.getString("session"));
                Transcript.setCourseCodeName(rs.getString("course_code_name"));
                Transcript.setFinalGradePoint(rs.getString("final_grade_point"));
                Transcript.setCredit(rs.getString("credit"));
                Transcript.setSgpa(rs.getString("Sgpa"));
                return Transcript;
            }
        });
    }

    public String getTranscriptNumber(String rollNumber) {
        String query = "SELECT transcript_number FROM student_program WHERE roll_number = ? GROUP BY roll_number";
        return cmsJdbcTemplate.queryForObject(query, new Object[]{rollNumber}, String.class);
    }

    public Integer getLastSerialNumber() {
        String query = "SELECT value FROM system_values WHERE code = 'TRNCPT'";
        return cmsJdbcTemplate.queryForObject(query, Integer.class);
    }

    public void updateLastSerialNumber(int newSerialNumber) {
        String query = "UPDATE system_values SET value = ? WHERE code = 'TRNCPT'";
        cmsJdbcTemplate.update(query, newSerialNumber);
    }

    public void updateStudentSerialNumber(String formattedSerialNumber, String rollNumber) {
        String query = "UPDATE student_program SET transcript_number = ? WHERE roll_number = ?";
        cmsJdbcTemplate.update(query, formattedSerialNumber, rollNumber);
    }

    public Integer countByRollNumber(String rollNumber) {
        String query = "SELECT COUNT(*) FROM student_program WHERE roll_number = ? ORDER BY program_completion_date DESC LIMIT 1";
        return cmsJdbcTemplate.queryForObject(query, new Object[]{rollNumber}, Integer.class);
    }

    public String getProgramStatus(String rollNumber) {
        String query = "SELECT program_status FROM student_program WHERE roll_number = ? ORDER BY program_completion_date DESC LIMIT 1";
        return cmsJdbcTemplate.queryForObject(query, new Object[]{rollNumber}, String.class);
    }

    public Transcript getTranscriptByRollNumber(String rollNumber) {
        String sql = "SELECT t1.FromDate, t1.ToDate, t2.duration, t2.medium, t2.roll_number, t2.student_first_name, t2.program_name, t2.enrollment_number, t2.date_of_birth, t2.cgpa " +
                     "FROM ( " +
                     "    SELECT srsh.roll_number, MIN(SUBSTRING(sp.registered_from_session, 1, 4)) AS FromDate, " +
                     "           MAX(SUBSTRING(sp.passed_to_session, 1, 4)) AS ToDate " +
                     "    FROM student_registration_semester_header srsh " +
                     "    JOIN program_course_header pch ON srsh.program_course_key = pch.program_course_key " +
                     "    JOIN student_program sp ON srsh.roll_number = sp.roll_number " +
                     "    AND pch.program_id = sp.program_id AND srsh.entity_id = sp.entity_id " +
                     "    AND pch.specialization_id = sp.specialization_id AND pch.branch_id = sp.branch_id " +
                     "    JOIN program_master pm ON pm.program_id = sp.program_id " +
                     "    JOIN student_master sm ON sm.enrollment_number = sp.enrollment_number " +
                     "    WHERE sp.program_status IN ('PAS', 'SWT') AND srsh.roll_number = ? " +
                     ") AS t1 " +
                     "JOIN ( " +
                     "    SELECT pm.months_duration_in_english AS duration, 'ENGLISH' AS medium, srsh.roll_number, "
                     + "sm.student_first_name,concat(pm.program_name,' ','(',if(stt1.component_description ='NONE','',stt1.component_description),')',' ',\r\n"
                     + "                         ' WITH SPECIALIZATION IN ',if(stt2.component_description='NONE','',stt2.component_description)) as program_name, sp.enrollment_number, sm.date_of_birth, sp.cgpa " +
                     "    FROM student_registration_semester_header srsh " +
                     "    JOIN program_course_header pch ON srsh.program_course_key = pch.program_course_key " +
                     "    JOIN student_program sp ON srsh.roll_number = sp.roll_number " +
                     "    AND pch.program_id = sp.program_id AND srsh.entity_id = sp.entity_id " +
                     "    AND pch.specialization_id = sp.specialization_id AND pch.branch_id = sp.branch_id " +
                     "    JOIN program_master pm ON pm.program_id = sp.program_id " +
                     "    JOIN student_master sm ON sm.enrollment_number = sp.enrollment_number" +
                     "  JOIN system_table_two stt1 on pch.branch_id = stt1.component_code  and stt1.group_code = 'BRNCOD'\r\n" +
                     "  JOIN system_table_two stt2 on pch.specialization_id = stt2.component_code  and stt2.group_code = 'SPCLCD' " +
                     "    WHERE sp.program_status = 'PAS' AND srsh.roll_number = ? " +
                     "    ORDER BY sp.program_completion_date DESC LIMIT 1 " +
                     ") AS t2 ON t1.roll_number = t2.roll_number";


        return cmsJdbcTemplate.queryForObject(sql, new Object[]{rollNumber, rollNumber}, new RowMapper<Transcript>() {
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
