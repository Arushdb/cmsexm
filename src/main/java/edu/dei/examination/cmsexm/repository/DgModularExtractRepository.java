package edu.dei.examination.cmsexm.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.cmsexm.model.DgModularExtract;
import edu.dei.examination.cmsexm.model.DgModularMain;

public interface DgModularExtractRepository extends JpaRepository<DgModularExtract, Integer> {
	
	
//this query is for inserting the data in the modular_students_sgpa before executing this code  insert into exam_live.modular_students_sgpa
  //  SELECT id + 210 AS id, session_start_date, roll_number, semester, SGPA, sgpa_list, program_code
//FROM (
//SELECT
//ROW_NUMBER() OVER () AS id,
//'2024-07-01' AS session_start_date,
//srsh.roll_number,
//'S1' AS semester,
//ROUND(SUM(sms.final_grade_point*cmps.credits)/SUM(cmps.credits),3) AS SGPA,
//GROUP_CONCAT(DISTINCT CONCAT(pch.semester_code,'-',srsh.sgpa) ORDER BY srsh.program_course_key) AS sgpa_list,
//'0001219' AS program_code
//FROM student_marks_summary sms
//JOIN course_master_per_session cmps
//ON sms.course_code = cmps.course_code
// AND sms.semester_start_date BETWEEN cmps.session_start_date AND cmps.session_end_date
// JOIN student_registration_semester_header srsh
//ON srsh.roll_number = sms.roll_number
// AND srsh.program_course_key = sms.program_course_key
// AND srsh.session_start_date = sms.semester_start_date
//AND srsh.entity_id = sms.entity_id
// JOIN program_course_header pch
// ON pch.program_course_key = srsh.program_course_key
// WHERE srsh.status = 'PAS'
// AND sms.program_course_key IN ('00012191500081', '00012191600005')
// AND semester_start_date IN ('2024-08-01','2024-10-15')
// AND srsh.roll_number IN (
 //  SELECT seta.roll_number
 //  FROM (
 //      SELECT roll_number, COUNT(*) AS passed_count
 //      FROM student_registration_semester_header
  //     WHERE program_course_key IN ('00012191500081', '00012191600005')
  //         AND session_start_date IN ('2024-08-01','2024-10-15')
  //         AND status = 'PAS'
  //     GROUP BY roll_number
  // ) seta
  // WHERE seta.passed_count = 2
//)
//GROUP BY sms.roll_number
//) t;
	//Actual Queries Start

	@Query(name ="getdgformat" ,nativeQuery = true,value = 
			" select * from dg_format order by sqno "			)
	List<Map<String, Object>> getdgformat();

	
	
	@Query(name = "getstudentlist" ,nativeQuery = true,value =
	    " select  'DAYALBAGH EDUCATIONAL INSTITUTE (DEEMED TO BE UNIVERSITY)' as org_name ,'' as org_name_l , s.academic_course_id, "
		+	" UPPER(s.course_name) as course_name ,s.branch ,s.specialization,  '' as course_name_l,concat(if(s.branch='NONE','',s.branch), "
		+	" if(s.specialization not in ('NONE'),concat(' with Specialization in ', "
		+	" if(s.specialization='NONE','',s.specialization)),'')) as stream,'' as stream_l, "
		+ "  concat(cast(s.passedSession as char(4)),'-',cast(s.passedToSession as char(4))) as session, "
		+	" s.enrollment_number as regn_no, s.roll_number as rroll, "
		+	" s.student_first_name as cname , s.gender as gender, s.dob as dob , s.father_first_name as fname, s.mother_first_name as mname, "
		+	" '' photo,'O' as mrks_rec_status, "
		+	" cast(s.passedToSession as char(4)) as year, '' as month,'' as division,'' as grade,'' as percent,s.doi, "
		+	" s.modular_group as sem, "
		 
		+   "  CASE when cast(s.EXAM_TYPE as char(4))='P' then 'PART TIME'  "
		+   "       when cast(s.EXAM_TYPE as char(4))='D' then 'DISTANCE'  "
		+   "       ELSE 'REGULAR' END  as exam_type,'' as tot_grade_points,'' as grand_tot_max,'' as grand_tot_mrks, "
		+	" '' as grand_tot_credit_points,"
		+   "    s.cgpa as cgpa,"
		+   "   s.remarks as remarks,"
		+   "   s.sgpa as sgpa, "
		+   "  s.ABC_ID as abc_account_id,'' as term_type,'' as tot_grade,s.AADHAAR_NAME ,"
		+   "   cast(substring(s.registered_from_session,1,4) as char(4)) as ADMISSION_YEAR "
		+	" from (select ss.result_declare_date as doi,  dn.name as 'course_name',pm.program_code as 'academic_course_id',pm.program_type as EXAM_TYPE, "
		+	" sm.student_first_name, sm.gender, date_format(sm.date_of_birth,'%d/%m/%Y') dob, sm.father_first_name,cast(setG.sgpa as char(6)) as SGPA, "
		+	" sm.mother_first_name, sp.roll_number, sp.enrollment_number, sp.entity_id, srsh.program_course_key, pch.program_id, "
		+	" br.component_description 'branch', "
		+	" spcl.component_description 'Specialization', pch.semester_code, sp.program_status,if(sp.current_semester = pch.semester_code "
		+	" and sp.program_status = 'PAS', sp.cgpa, '') as cgpa, "
		+	" sp.passed_to_session, year(pr.session_start_date) passedSession, year(pr.session_end_date) passedToSession,setG.modular_group, "
		+	" sp.current_semester ,abc.ABC_ID ,abc.AADHAAR_NAME ,sa.theory_sgpa,sa.practical_sgpa,"
		+ "sp.registered_from_session,sp.theory_cgpa,sp.practical_cgpa,setG.remarks from "
		
		+	"  cms_live.student_registration_semester_header as srsh  "
		+	" join cms_live.program_course_header as   pch on pch.program_course_key = srsh.program_course_key "
		+	" join cms_live.student_program as sp on srsh.roll_number = sp.roll_number and srsh.status = 'PAS'  "
		+   " and sp.program_id = pch.program_id and sp.branch_id = pch.branch_id "
		+   " and sp.specialization_id = pch.specialization_id and sp.entity_id = srsh.entity_id"
		+ "  join cms_live.program_registration as pr on pr.entity_id = srsh.entity_id and pr.program_course_key = srsh.program_course_key "
		+   "		and pr.semester_start_date = srsh.session_start_date "
		+	" and pr.semester_end_date = srsh.session_end_date "
		+	" join cms_live.system_table_two as br on br.component_code = sp.branch_id and br.group_code = 'BRNCOD' " 
		+	" join cms_live.system_table_two as spcl on spcl.component_code = sp.specialization_id and spcl.group_code = 'SPCLCD' "
		+	" join cms_live.entity_master as em on em.entity_id = sp.entity_id " 
		+	" join cms_live.program_master as pm on pm.program_id = sp.program_id " 
		+	" join cms_live.student_master as sm on sm.enrollment_number = sp.enrollment_number " 
		+   " join cms_live.student_aggregate sa on sa.roll_number = srsh.roll_number and sa.program_course_key =srsh.program_course_key and "
		+   " sa.semester_start_date =srsh.session_start_date and sa.semester_end_date = srsh.session_end_date "
		+	" join cms_live.degree_name as dn on dn.id = sp.program_id and dn.group_code = 'PROGRM' "
		+	" join cms_live.student_scrutiny ss on ss.program_course_key = srsh.program_course_key and "
		+	"          ss.semester_start_date = srsh.session_start_date and ss.roll_number = '*' "
		+   " join (select mss.remarks,mss.modular_group, mss.sgpa,mss.roll_number,ms.program_course_key,ms.module,ms.session_start_date,ms.session_end_date "
	    +   " from exam_live.dg_modular_controller dmc join exam_live.modular_sem ms on dmc.program_id =ms.program_id "
	    +   "  and ms.module_group =dmc.module_group "
	    +   "  join exam_live.modular_students_sgpa mss on mss.program_id=ms.program_id and ms.module_group = mss.modular_group )setG "
	    +   "  on setG.roll_number =srsh.roll_number and setG.module =pch.semester_code and setG.program_course_key=srsh.program_course_key "
	    +   "  and setG.session_start_date = srsh.session_start_date and setG.session_end_date =srsh.session_end_Date "
		+   " left join dg_abc_id as abc on abc.REGN_NO=sm.enrollment_number "
		+	" where pch.program_course_key in (?1, ?2) and srsh.session_start_date in (?3,?4) and srsh.entity_id =?5  "
		+	" group by srsh.entity_id, srsh.roll_number)s "

)
	List<Map<String, Object>> getstudentlist(String pck1, String pck2, Date ssd1, Date ssd2,String entityId);
	
	@Query(name = "getsubjects" ,nativeQuery = true,value =
	
	" select srsh.roll_number,cmps.course_name,sc.course_code,cast(sms.final_grade_point as char(6) ) as gradepoint ,cast(cmps.credits as char(7)) as credits ,cast(sms.final_grade_point*cmps.credits as char(7) )as creditpoint from "
   +" cms_live.student_registration_semester_header as srsh  "
	+" join cms_live.student_course as sc on sc.roll_number = srsh.roll_number and sc.entity_id = srsh.entity_id and sc.program_course_key = srsh.program_course_key "
	+" and sc.semester_start_date = srsh.session_start_date and sc.semester_end_date = srsh.session_end_date and sc.student_status = 'PAS' "
	+" join cms_live.student_marks_summary as sms on sms.roll_number = srsh.roll_number and sms.program_course_key = srsh.program_course_key "
	+ "and sms.entity_id = srsh.entity_id "
	+" and sms.course_code = sc.course_code and sms.semester_start_date = srsh.session_start_date and "
	+" sms.semester_end_date = srsh.session_end_date "
	+" join cms_live.course_master_per_session as cmps on cmps.course_code = sc.course_code and sms.semester_start_date "
	+" between cmps.session_start_date and cmps.session_end_date "
	+" where srsh.roll_number =?1 and srsh.program_course_key in(?2,?3)  and srsh.status = 'PAS' "
	+" order by  sc.course_code ")
	List<Map<String, Object>> getsubjectlist(String rollno,String pck1, String pck2);
	
	
	
	@Query(name = "getmaxsub" ,nativeQuery = true,value =
	 " select roll_number,cast(count(*)  as char(2))as totsub " 
	+ " from cms_live.student_course where program_course_key in(?1,?2) "
	+ " and semester_start_date in (?3,?4) "
	+ " group by roll_number "
	+ " order by cast(totsub as SIGNED)  desc limit 1 ")
	List<Map<String, Object>> getmaxsubject(String pck1, String pck2, Date ssd1, Date ssd2);
	
	@Query(name = "gettotcreditpoint" ,nativeQuery = true,value =
	" select  cast(round(sum(sms.final_grade_point*cmps.credits),3) as char(7)) TOT_CREDIT_POINTS,cast(sum(cmps.credits) as char(7)) as TOT_CREDIT from "
	+" cms_live.student_registration_semester_header as srsh " 
	+" join cms_live.student_course as sc on sc.roll_number = srsh.roll_number and sc.entity_id = srsh.entity_id and sc.program_course_key = srsh.program_course_key "
	+" and sc.semester_start_date = srsh.session_start_date and sc.semester_end_date = srsh.session_end_date and sc.student_status = 'PAS' "
	+" join cms_live.student_marks_summary as sms on sms.roll_number = srsh.roll_number and sms.program_course_key = srsh.program_course_key and sms.entity_id = srsh.entity_id "
	+" and sms.course_code = sc.course_code and sms.semester_start_date = srsh.session_start_date and "
	+" sms.semester_end_date = srsh.session_end_date "
	+" join cms_live.course_master_per_session as cmps on cmps.course_code = sc.course_code and sms.semester_start_date between cmps.session_start_date and cmps.session_end_date "
	+" where  srsh.roll_number =?1  and  srsh.program_course_key in(?2,?3)   and srsh.status = 'PAS' "
	+" group by  srsh.roll_number ")
	
	Map<String, Object> gettotcreditpoint(String rollno,String pck1, String pck2);
	
	@Modifying
    @Query(nativeQuery = true, value =
            "UPDATE dg_modular_controller SET status='C', run_time=?1 " +
            "WHERE program_id=?2 AND module_group=?3 AND semester_start_date=?4")
    int updatestatus(Date runtime, String program_id, String module_group, Date semester_start_date);
	
}
