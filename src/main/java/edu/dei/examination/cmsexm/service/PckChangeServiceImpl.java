package edu.dei.examination.cmsexm.service;

import edu.dei.examination.cmsexm.model.PckChange;
import edu.dei.examination.cmsexm.repository.PckChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PckChangeServiceImpl implements PckChangeService {

    @Autowired
    private PckChangeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)  // Ensure rollback for all exceptions
    @Scheduled(fixedRateString = "${run-frquency.minutes}", timeUnit = java.util.concurrent.TimeUnit.MINUTES)
    @Override
    public void processPckChanges() {
        // Get all records with status 'N'
        List<PckChange> changes = repository.findByStatus("N");
        for (PckChange change : changes) {
        	try {
            String oldPck = change.getOldPck();
            String newPck = change.getNewPck();
            Date ssd = change.getSemesterStartDate();
            Date sed = change.getSemesterEndDate();
            String programId = change.getProgramId();
            String branchId = change.getBranchId();
            String specializationId = change.getSpecializationId();
            String semester = change.getSemester();
            Long id = change.getId();

            // 1. Insert into student_registration_semester_header
            String query1 = "INSERT INTO cms_live.student_registration_semester_header " +
                    "SELECT register_Date, number_of_remedials, status, insert_time, modification_time, creator_id, modifier_id, " +
                    "roll_number, session_start_date, session_end_date, attempt_number, total_credit_earned, sgpa, weighted_percentage, " +
                    "student_process_status, register_due_date, entity_id, ?, registered_credit, " +
                    "registered_theory_credit_excluding_audit, registered_practical_credit_excluding_audit, " +
                    "registration_credit_excluding_audit, reason_description, switch_type, switch_rule, old_status " +
                    "FROM cms_live.student_registration_semester_header srsh " +
                    "WHERE srsh.program_course_key = ? AND srsh.session_start_date = ? AND NOT EXISTS ("
                    + "SELECT 1 FROM cms_live.student_registration_semester_header e "
                    + "WHERE e.program_course_key = ? AND e.roll_number = srsh.roll_number AND e.session_start_date = ?"
                    + "      )";
            jdbcTemplate.update(query1, newPck, oldPck, ssd,newPck,ssd);
            
            //2. Insert into course_evaluation_component
            String query2="insert into cms_live.course_evaluation_component"
            		+ "            select  ?, cec.exam_date, cec.date_from_display_marks, cec.date_to_display_marks, cec.evaluation_id, cec.evaluation_id_name,"
            		+ "             cec.group_id, cec.rule, cec.order_in_awardsheet, cec.course_code, cec.maximum_marks,insert_time,modification_time,creator_id,"
            		+ "             modifier_id, publish_marks, weightage, component_full_name, published_by, published_time, component_type "
            		+ "             from (select program_id,course_code from cms_live.student_course sc join cms_live.program_course_header pch on sc.program_course_key=pch.program_course_key "
            		+ "            where sc.program_course_key = ? "
            		+ "            and semester_start_date = ? group by sc.course_code)seta"
            		+ "            join cms_live.course_evaluation_component cec on cec.course_code =seta.course_code"
            		+ " and seta.program_id =cec.program_id   AND NOT EXISTS ("
            		+ "          SELECT 1 FROM cms_live.course_evaluation_component e "
            		+ "          WHERE e.program_id = ? AND e.course_code = seta.course_code )";

            		jdbcTemplate.update(query2, programId, oldPck, ssd,programId);
            		
            		//3. Update semester_processing_control
            		 String query3 = "UPDATE semester_processing_control " +
                             "SET status = 'COM' " +
                             "WHERE program_course_key = ? AND semester_start_date = ? AND process = 'SEP'";
        jdbcTemplate.update(query3, oldPck, ssd);
        
      //3. Update semester_processing_control
		 String pcdquery = "INSERT INTO program_course_detail" +
				"select ?, course_code, course_category, available, course_group," +
				 "insert_time, modification_time, creator_id, modifier_id, learning_mode" +
				"from program_course_detail where program_course_key = ? " ;
         jdbcTemplate.update(pcdquery,  newPck, oldPck);

           

            // 4. Insert into student_course
            String query4 = "INSERT INTO cms_live.student_course " +
                    "SELECT roll_number, ?, semester_start_date, semester_end_date, course_code, course_name, orginal_course_code, " +
                    "course_status, student_status, insert_time, modification_time, creator_id, modifier_id, " +
                    "attempt_number, course_group, entity_id, old_student_status, credits " +
                    "FROM cms_live.student_course WHERE program_course_key = ? AND semester_start_date = ?" +
                    "AND NOT EXISTS ( " +
                    "    SELECT 1 FROM cms_live.student_course existing " +
                    "    WHERE existing.roll_number = sc.roll_number AND existing.program_course_key = ? " +
                    "    AND existing.semester_start_date = sc.semester_start_date AND existing.course_code = sc.course_code " +
                    ")";
            jdbcTemplate.update(query4, newPck, oldPck, ssd, newPck);
           

            // 5. Insert into student_marks_summary
            String query5 = "INSERT INTO cms_live.student_marks_summary " +
                    "SELECT university_code, entity_id, roll_number, ?, semester_start_date, semester_end_date, " +
                    "total_internal, total_external, total_marks, course_code, internal_grade, external_grade, final_grade_point, " +
                    "insert_time, modification_time, creator_id, modifier_id, earned_credits, publish_grades, ref_no, remarks, grace_marks " +
                    "FROM cms_live.student_marks_summary WHERE program_course_key = ? AND semester_start_date = ?" +
                    "AND NOT EXISTS ( " +
                    "    SELECT 1 FROM cms_live.student_marks_summary existing " +
                    "    WHERE existing.roll_number = sms.roll_number AND existing.program_course_key = ? " +
                    "    AND existing.semester_start_date = sms.semester_start_date AND existing.course_code = sms.course_code " +
                    ")";
            jdbcTemplate.update(query5, newPck, oldPck, ssd, newPck);
            

            // 6. Insert into student_marks
            String query6 = "INSERT INTO cms_live.student_marks " +
                    "SELECT university_code, entity_id, roll_number, ?, evaluation_id, marks, old_marks, grades, pass_fail, status, " +
                    "course_code, semester_start_date, semester_end_date, insert_time, modification_time, creator_id, modifier_id, " +
                    "attempt_number, requested_marks, requester_remarks, issue_status, teacher_remarks, attendence " +
                    "FROM cms_live.student_marks WHERE program_course_key = ? AND semester_start_date = ?" +
                    "AND NOT EXISTS ( " +
                    "    SELECT 1 FROM cms_live.student_marks existing " +
                    "    WHERE existing.roll_number = sm.roll_number " +
                    "    AND existing.program_course_key = ? " +
                    "    AND existing.evaluation_id = sm.evaluation_id " +
                    "    AND existing.course_code = sm.course_code " +
                    "    AND existing.semester_start_date = sm.semester_start_date " +
                    ")";
            jdbcTemplate.update(query6, newPck, oldPck, ssd, newPck);
            

            // 7. Insert into student_aggregate
            String query7 = "INSERT INTO cms_live.student_aggregate " +
                    "SELECT university_code, roll_number, semester_start_date, semester_end_date, theory_weighted_percentage, practical_weighted_percentage, " +
                    "remarks, earned_practical_credit, earned_theory_credit, ?, point_secured_theory_sgpa, point_secured_practical_sgpa, " +
                    "point_secured_theory_cgpa, point_secured_practical_cgpa, earned_theory_credit_cgpa, earned_practical_credit_cgpa, " +
                    "earned_theory_aud_credit, earned_practical_aud_credit, theory_sgpa, practical_sgpa, sgpa, weighted_percentage, cgpa, " +
                    "theorycgpa, practicalcgpa, entity_id " +
                    "FROM cms_live.student_aggregate WHERE program_course_key = ? AND semester_start_date = ?" +
                    "AND NOT EXISTS ( " +
                    "    SELECT 1 FROM cms_live.student_aggregate existing " +
                    "    WHERE existing.roll_number = sa.roll_number " +
                    "    AND existing.program_course_key = ? " +
                    "    AND existing.semester_start_date = sa.semester_start_date " +
                    ")";
            jdbcTemplate.update(query7, newPck, oldPck, ssd, newPck);
            

            // 8. Insert into student_program
            if ("SM1".equalsIgnoreCase(semester) || "SM5".equalsIgnoreCase(semester)) {
            String query8 = "INSERT INTO cms_live.student_program " +
                    "SELECT sp.cgpa, sp.enrollment_number, sp.roll_number, sp.register_date, sp.program_completion_date, sp.current_semester, " +
                    "sp.program_status, sp.insert_time, sp.modification_time, sp.creator_id, sp.modifier_id, sp.entity_id,?, " +
                    "sp.branch_id, sp.specialization_id, sp.switch_number, sp.sequence_number, sp.in_semester, sp.out_semester, " +
                    "sp.mode_of_entry, sp.division, sp.switched_date, sp.theory_cgpa, sp.practical_cgpa, sp.theory_cwp, sp.practical_cwp, " +
                    "sp.cumulative_wp, sp.registered_from_session, sp.passed_from_session, sp.passed_to_session, " +
                    "sp.registered_to_session, sp.theory_division, sp.practical_division, sp.reason_description, sp.university_code, sp.transcript_number " +
                    "FROM cms_live.student_registration_semester_header srsh " +
                    "JOIN cms_live.program_course_header pch ON srsh.program_course_key = pch.program_course_key " +
                    "JOIN cms_live.student_program sp ON sp.roll_number = srsh.roll_number AND sp.program_id = pch.program_id AND " +
                    "sp.branch_id = pch.branch_id AND sp.specialization_id = pch.specialization_id AND sp.entity_id = srsh.entity_id " +
                    "WHERE srsh.program_course_key = ? AND srsh.session_start_date = ? " +
                    "AND NOT EXISTS ( " +
                    "    SELECT 1 FROM cms_live.student_program existing " +
                    "    WHERE existing.roll_number = sp.roll_number AND existing.program_id = pch.program_id " +
                    "    AND existing.branch_id = pch.branch_id AND existing.specialization_id = pch.specialization_id " +
                    "    AND existing.entity_id = sp.entity_id" +
                    ")";

            jdbcTemplate.update(query8,programId, oldPck, ssd);
            }

            // 9. Insert into semester_processing_control
            String query9 = "INSERT INTO cms_live.semester_processing_control " +
                    "SELECT entity_id, semester_start_date, semester_end_date, status, insert_time, modification_time, " +
                    "creator_id, modifier_id, process, process_start_date, process_end_date, ? " +
                    "FROM cms_live.semester_processing_control WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query9, newPck, oldPck, ssd);

            // 10. Insert into activity_master
            String query10 = "INSERT INTO cms_live.activity_master " +
                    "SELECT entity_id, semester_start_date, semester_end_date, process, activity, activity_sequence, process_activity_start_date, " +
                    "process_activity_end_date, activity_status, insert_time, modification_time, creator_id, modifier_id, ?, status, session_start_date, session_end_date " +
                    "FROM cms_live.activity_master WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query10, newPck, oldPck, ssd);

            // 11. Insert into instructor_course
            String query11 = "INSERT INTO cms_live.instructor_course " +
                    "SELECT ?, employee_id, semester_start_date, semester_end_date, insert_time, modification_time, creator_id, modifier_id, " +
                    "entity_id, course_code, status, assigned_by, assigned_time, display_type " +
                    "FROM cms_live.instructor_course WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query11, newPck, oldPck, ssd);

            // 12. Insert into course_marks_approval_status
            String query12 = "INSERT INTO cms_live.course_marks_approval_status " +
                    "SELECT ?, entity_id, approval_order, status, request_sender, request_getter, request_date, completion_date, withdrawl_date, semester_start_date, semester_end_date, course_code, display_type, " +
                    "reason, creator_id, insert_time, modifier_id, modification_time, submit_dates " +
                    "FROM cms_live.course_marks_approval_status WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query12, newPck, oldPck, ssd);

            // 13. Insert into course_marks_approval
            String query13 = "INSERT INTO cms_live.course_marks_approval " +
                    "SELECT cma.entity_id, ?, cma.course_code, cma.employee_id, cma.approval_order, cma.sequence_number, cma.insert_time, cma.modification_time, cma.creator_id, cma.modifier_id, cma.display_type " +
                    "FROM cms_live.instructor_course ic JOIN cms_live.course_marks_approval cma ON ic.program_course_key = cma.program_course_key " +
                    "AND ic.course_code = cma.course_code AND ic.employee_id = cma.employee_id AND ic.display_type = cma.display_type " +
                    "AND ic.entity_id = cma.entity_id WHERE ic.program_course_key = ? AND ic.semester_start_date = ?";
            jdbcTemplate.update(query13, newPck, oldPck, ssd);

            // 14. Insert into student_marks_summary_rem
            String query14 = "INSERT INTO cms_live.student_marks_summary_rem " +
                    "SELECT university_code, entity_id, roll_number, ?, semester_start_date, semester_end_date, total_internal, total_external, total_marks, course_code, internal_grade, external_grade, final_grade_point, " +
                    "insert_time, modification_time, creator_id, modifier_id, earned_credits, publish_grades, ref_no, remarks, grace_marks " +
                    "FROM cms_live.student_marks_summary_rem WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query14, newPck, oldPck, ssd);

            // 15. Insert into student_scrutiny
            String query15 = "INSERT INTO cms_live.student_scrutiny " +
                    "SELECT entity_id, ?, roll_number, semester_start_date, semester_end_date, scrutiny, result_declare_date, creator_id, insert_time, modifier_id, modification_time, remedial_result_declare_date " +
                    "FROM cms_live.student_scrutiny WHERE program_course_key = ? AND semester_start_date = ?";
            jdbcTemplate.update(query15, newPck, oldPck, ssd);
            
            

            // After all queries execute successfully, update the record in pck_change_controller to mark it processed (status = 'P')
            String updateQuery = "UPDATE exam_live.pck_change_controller SET status = 'P' WHERE id = ?";
            jdbcTemplate.update(updateQuery, id);
        	} catch (Exception e) {
                // Transaction will auto rollback due to @Transactional
                // Optional: Add logging here
                throw e; // Required to propagate for rollback
            }
        }
    }
}
