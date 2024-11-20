package edu.dei.examination.cmsexm.repository;

import edu.dei.examination.cmsexm.model.TranscriptData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TranscriptRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // SQL Query
    private final String query = "SELECT srsh.sgpa Sgpa,srsh.roll_number, " +
            "substring(pch.semester_code,3,2) as sem, " +
            "concat(substring(pr.session_start_date,1,4), '-', substring(pr.session_end_date,1,4)) as session, " +
            "concat(sc.course_code, ':', cmps.course_name) as course_code_name, " +
            "sms.final_grade_point, " +
            "cmps.credits as credit " +
            "FROM cms_live.student_registration_semester_header srsh " +
            "JOIN cms_live.program_course_header pch ON pch.program_course_key = srsh.program_course_key " +
            "JOIN cms_live.student_course sc ON sc.program_course_key = srsh.program_course_key " +
            "AND sc.roll_number = srsh.roll_number AND sc.entity_id = srsh.entity_id " +
            "AND srsh.session_start_date = sc.semester_start_date " +
            "AND srsh.session_end_date = sc.semester_end_date " +
            "JOIN cms_live.student_marks_summary sms ON sms.roll_number = sc.roll_number " +
            "AND sms.program_course_key = sc.program_course_key " +
            "AND sc.course_code = sms.course_code " +
            "AND sc.semester_start_date = sms.semester_start_date " +
            "AND sc.semester_end_date = sms.semester_end_date " +
            "AND sc.entity_id = sms.entity_id " +
            "JOIN cms_live.program_registration pr ON pr.program_course_key = srsh.program_course_key " +
            "AND srsh.session_start_date = pr.semester_start_date " +
            "AND srsh.session_end_date = pr.semester_end_date " +
            "AND srsh.entity_id = sc.entity_id " +
            "JOIN cms_live.course_master_per_session cmps ON cmps.course_code = sms.course_code " +
            "AND cmps.session_start_date = pr.session_start_date " +
            "AND cmps.session_end_date = pr.session_end_date " +
            "JOIN cms_live.student_program sp ON sp.roll_number = srsh.roll_number " +
            "AND sp.program_id = pch.program_id " +
                       
            "WHERE srsh.roll_number = ? AND sp.program_status = 'PAS' AND srsh.status = 'PAS' " +
            "group by sc.semester_start_date,sc.course_code order by sc.semester_start_date,sc.course_code";

    // Method to get Transcript data based on Roll Number
    public List<TranscriptData> getTranscriptData(String roll_number) {
        return jdbcTemplate.query(query, new Object[]{roll_number}, new RowMapper<TranscriptData>() {
            @Override
            public TranscriptData mapRow(ResultSet rs, int roll_number) throws SQLException {
            	TranscriptData transcriptData = new TranscriptData();
            	transcriptData.setRoll_number(rs.getString("roll_number"));
                transcriptData.setSem(rs.getString("sem"));
                transcriptData.setSession(rs.getString("session"));
                transcriptData.setCourseCodeName(rs.getString("course_code_name"));
                transcriptData.setFinalGradePoint(rs.getString("final_grade_point"));
                transcriptData.setCredit(rs.getString("credit"));
                transcriptData.setSgpa(rs.getString("Sgpa"));
                return transcriptData;
            }
        });
    }
}

