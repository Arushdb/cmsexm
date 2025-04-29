package edu.dei.examination.cmsexm.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.cmsexm.model.DegreeDgExtract;
import edu.dei.examination.cmsexm.model.DegreeDgMain;

public interface DegreeDgExtractRepository extends JpaRepository<DegreeDgExtract, Integer> {
	
	
	@Query(name ="getdegreedgformat" ,nativeQuery = true,value = 
			" select * from degree_dg_format order by sqno "			)
	List<Map<String, Object>> getdegreedgformat();

	
	
	@Query(name = "getdegreestudentlist" ,nativeQuery = true,value =
			"select  'DAYALBAGH EDUCATIONAL INSTITUTE (DEEMED TO BE UNIVERSITY)' as ORG_NAME  , s.academic_course_id as ACADEMIC_COURSE_ID," 
					+"			concat (ucase(s.course_name),if(s.branch='NONE','',concat(' in ',s.branch)),"
					+ "							if(s.specialization not in ('NONE'), concat(' with Specialization in ',s.specialization),'')) as COURSE_NAME,'' as SUB_COURSE_NAME,"
					+"			 s.enrollment_number as REGN_NO, s.roll_number as RROLL, "
					+"			 s.student_first_name as CNAME , s.gender as GENDER, s.dob as DOB , CAST('O' AS CHAR) as MRKS_REC_STATUS," 
					+"			 cast(s.passedToSession as char(4)) as YEAR, '' as MONTH,ucase(s.division1) as DIVISION,'' as GRADE,s.doi as DOI," 
					+"		       if(s.program_id = '0001009',concat('T',' ',s.theory_cgpa,'  ','P',' ',s.practical_cgpa),s.cgpa) as CGPA,"
					+"			s.ABC_ID as ABC_ACCOUNT_ID,'' as SUB1NM,s.AADHAAR_NAME ,'' as DIVISION_TH,'' as DIVISION_PR "
					+"		      from (select cd.convocation_date as doi,  dn.name as 'course_name',pm.program_code as 'academic_course_id',pm.program_type as EXAM_TYPE, " 
					+"			 sm.student_first_name, sm.gender, date_format(sm.date_of_birth,'%d/%m/%Y') dob, sm.father_first_name,cast(srsh.sgpa as char(6)) as SGPA, "
					+"			 sm.mother_first_name, sp.roll_number, sp.enrollment_number, sp.entity_id, srsh.program_course_key, pch.program_id, "
					+"			 br.component_description 'branch', if((dvs.component_description = 'First with Distinction'),'First Division with Distinction',concat(dvs.component_description,' ','Division')) 'division1',"
					+"			 spcl.component_description 'Specialization', pch.semester_code, sp.program_status,if(sp.program_status = 'PAS', sp.cgpa, '') as cgpa, sp.division,"
					+"			 sp.passed_to_session, year(pr.session_start_date) passedSession, year(pr.session_end_date) passedToSession," 
					+"			 sp.current_semester ,abc.ABC_ID ,abc.AADHAAR_NAME ,sa.theory_sgpa,sa.practical_sgpa,sp.registered_from_session,sp.theory_cgpa,sp.practical_cgpa from "
							
					+"			  cms_live.student_registration_semester_header as srsh"  
					+"			 join cms_live.program_course_header as   pch on pch.program_course_key = srsh.program_course_key "
					+"			 join cms_live.student_program as sp on srsh.roll_number = sp.roll_number and srsh.status = 'PAS' "  
					+"		    and sp.program_id = pch.program_id and sp.branch_id = pch.branch_id "
					+"		    and sp.specialization_id = pch.specialization_id and sp.entity_id = srsh.entity_id "
					+"		   join cms_live.program_registration as pr on pr.entity_id = srsh.entity_id and pr.program_course_key = srsh.program_course_key "
					+"		   		and pr.semester_start_date = srsh.session_start_date "
					+"			 and pr.semester_end_date = srsh.session_end_date "
					+"			 join cms_live.system_table_two as br on br.component_code = sp.branch_id and br.group_code = 'BRNCOD' " 
					+"			 join cms_live.system_table_two as spcl on spcl.component_code = sp.specialization_id and spcl.group_code = 'SPCLCD' "
					+"			 join cms_live.entity_master as em on em.entity_id = sp.entity_id " 
					+"			 join cms_live.program_master as pm on pm.program_id = sp.program_id "
					+"			 join cms_live.student_master as sm on sm.enrollment_number = sp.enrollment_number " 
					+"		    join cms_live.student_aggregate sa on sa.roll_number = srsh.roll_number and sa.program_course_key =srsh.program_course_key and "
					+"		    sa.semester_start_date =srsh.session_start_date and sa.semester_end_date = srsh.session_end_date "
					+"			 join cms_live.degree_name as dn on dn.id = sp.program_id and dn.group_code = 'PROGRM' "
					+"			 join cms_live.student_scrutiny ss on ss.program_course_key = srsh.program_course_key and "
					+"			          ss.semester_start_date = srsh.session_start_date and ss.roll_number = '*' "
					+"			join cms_live.system_table_two  as dvs on dvs.component_code = sp.division and dvs.group_code = 'DVSCOD'"
					+"		    join convocation_dates cd on sp.passed_from_session =cd.passed_from_session left join dg_abc_id as abc on abc.REGN_NO=sm.enrollment_number "
					+"			 where sp.program_id = ?1  and sp.passed_from_session = ?2 and program_status = 'PAS' "
					+"			 group by  srsh.roll_number,sp.program_id)s ; ")
	List<Map<String, Object>> getdegreestudentlist(String programId ,Date ssd);
	
	
	
	@Query(name = "updatestatus" ,nativeQuery = true,value =
	" update  degree_dg_controller set  status = 'C' , run_date =?1 "
	+" where program_id=?2 " 
	+" and session_start_date=?3 ") 
	@Modifying
	int updatestatus(Date rundate,String programId ,Date ssd);
	
	
}
