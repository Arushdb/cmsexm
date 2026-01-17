package edu.dei.examination.phd.service;




import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.dto.ScholarDTO;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScholarService {

	 @PersistenceContext(unitName = "phd") 
    private EntityManager em;

    @Transactional("phdTransactionManager")
    public int createScholarsForAcademicYearAndMonth(CreateScholarsRequest request) {
    	Object dbName = em
    		    .createNativeQuery("SELECT DATABASE()")
    		    .getSingleResult();
    		System.out.println("Connected DB = " + dbName);
    		
                  
        List<ScholarDTO> list=em.createNamedQuery("Applicant.selectForScholar", ScholarDTO.class)
        		.setParameter("year", request.getAcademicYear())
        		.setParameter("month", request.getAdmissionMonth())
        		.getResultList();

        if (list.isEmpty()) {
            return 0;
        }

        String insertSql =
                "INSERT INTO scholars (program_id,  full_name, gender_id, email, date_of_birth,phone, category, admission_date, status_id, \r\n"
                + " created_at,application_number) " +
                "VALUES (:program_id, :first_name, :gender,:email,:dob,:phone,:category,:selection_date,1,now(),:appno)";

        LocalDateTime now = LocalDateTime.now();

        for (ScholarDTO row : list) {
            String appno =  row.getAppno();
            

            em.createNativeQuery(insertSql)
                    .setParameter("appno", row.getAppno()) 
                    .setParameter("program_id", row.getProgramid())
                    .setParameter("first_name", row.getFirstName())
                    .setParameter("gender", row.getGender_id())
                    .setParameter("email", row.getEmail()) 
                    .setParameter("dob", row.getDob())
                    .setParameter("phone", row.getPhone())
                    .setParameter("category", row.getCategory())
                    .setParameter("selection_date", row.getAdmissionDate())
                              
                    .executeUpdate();
        }

        return list.size();
    }

    private String generateScholarNumber(Object[] row) {
        // row[2] = academic_year , row[0] = id  (index based on SELECT order)
        return "SCH-" + row[2] + "-" + row[0];
    }
}
