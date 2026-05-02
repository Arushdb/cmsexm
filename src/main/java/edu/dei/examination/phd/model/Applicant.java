package edu.dei.examination.phd.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;

import edu.dei.examination.phd.dto.ScholarDTO;

import javax.persistence.ConstructorResult;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.ColumnResult;


@Entity
@NamedNativeQuery(
    name = "Applicant.selectForScholar",
    query = " select a.application_number as appno,first_name as firstName ,primary_email_id as email,date_of_birth as dob,category_code as category ,"
    		+ " CASE "
    		+ "        WHEN gender = 'F' THEN 2  "
    		+ "        WHEN gender = 'M' THEN 1  "
    		+ "        ELSE 0  "
    		+ "    END AS gender_id, "
    		
    	  + " program_id as programid,home_phone as phone ,selection_date  as admissionDate ,a.department_id from phd_selected_candidates a " 
    	  + " join admlive_061225.entity_student b on a.application_number=b.application_number "
    	  + " and  a.academic_year=b.registered_in_session" 
    	  + " join admlive_061225.admission_addresses_master aam  on aam.user_id=b.student_id"
    	  + " WHERE academic_year = :year AND admission_month = :month  and address_key='PER' and a.status='ACT' ",
    resultSetMapping = "ScholarDTOMapping"
)
@NamedNativeQuery(name = "Applicant.getCMSScholars",
query=" select a.enrolmentno ,student_first_name as firstName ,primary_email_id as email,"
		+ " date_of_birth as dob,category_code as category ,"
		+ "    		 CASE \"\r\n"
		+ "    		        WHEN gender = 'F' THEN 2  "
		+ "    		        WHEN gender = 'M' THEN 1  "
		+ "    		        ELSE 0  "
		+ "    		    END AS gender_id, "
		  	
		+ "    	   a.program_id as programid,home_phone as phone ,null  as admissionDate ,a.department_id \r\n"
		+ "		  from phd_cms_scholars a "
		+ "    	  join cms_live.student_master b on a.enrolmentno=b.enrollment_number "
		+ "		   join cms_live.addresses_master c on b.student_id=c.user_id "
		+ "    	   and  a.academic_year=b.registered_in_session  ",
resultSetMapping = "ScholarDTOMapping"
		)


@SqlResultSetMapping(
    name = "ScholarDTOMapping",
    classes = @ConstructorResult(
        targetClass = ScholarDTO.class,
        columns = {
            @ColumnResult(name = "appno", type = String.class),
            @ColumnResult(name = "firstName", type = String.class),
            @ColumnResult(name = "category", type = String.class),
            @ColumnResult(name = "email", type = String.class),
            @ColumnResult(name = "phone", type = String.class),
            @ColumnResult(name = "gender_id", type = Integer.class),
            @ColumnResult(name = "dob", type = LocalDate.class),
            @ColumnResult(name = "programid", type = Integer.class),
            @ColumnResult(name = "admissionDate", type = LocalDate.class),
            @ColumnResult(name = "department_id", type = Integer.class)
            
        }
    )
)
public class Applicant {
    @Id
    private Long applicationNo;
}

