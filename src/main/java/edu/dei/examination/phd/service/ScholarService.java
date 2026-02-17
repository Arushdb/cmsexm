package edu.dei.examination.phd.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.dto.ScholarDTO;
import edu.dei.examination.phd.dto.ScholarDashboardDTO;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.repository.ScholarsRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ScholarService {
	
	@Autowired
	ScholarsRepository thescholarsRepository;

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
    
    @Transactional(readOnly = true, transactionManager = "phdTransactionManager")
    public ScholarDashboardDTO getScholarDashboard(int scholarId) {

        // ---------- 1. Fetch Scholar Info ----------
        Object[] scholarRow = (Object[]) em.createNativeQuery(
                "SELECT s.application_number, s.full_name, s.category, " +
                "       s.email, s.phone, s.gender_id, s.date_of_birth, " +
                "       s.program_id, s.admission_date ,p.name as programname" +
               
                " FROM scholars s  join programs p on p.program_id=s.program_id" +
                " WHERE s.scholar_id = :scholarId")
            .setParameter("scholarId", scholarId)
            .getSingleResult();

        ScholarDTO scholarDTO = new ScholarDTO();
        scholarDTO.setAppno((String) scholarRow[0]);
        scholarDTO.setFirstName((String) scholarRow[1]);
        scholarDTO.setCategory((String) scholarRow[2]);
        scholarDTO.setEmail((String) scholarRow[3]);
        scholarDTO.setPhone((String) scholarRow[4]);
        scholarDTO.setGender_id(((Number) scholarRow[5]).intValue());
        scholarDTO.setDob(((java.sql.Date) scholarRow[6]).toLocalDate());
        scholarDTO.setProgramid(((Number) scholarRow[7]).intValue());
        scholarDTO.setAdmissionDate(((java.sql.Date) scholarRow[8]).toLocalDate());
        scholarDTO.setProgramname((String)scholarRow[9]);

        // ---------- 2. Fetch Latest Progress Report ----------
        List<Object[]> reportList = em.createNativeQuery(
                " SELECT pr.id, pr.semester_registration_id , pr.progress_status  , " +
                " pr.next_actions, pr.insert_time ,s.semester_name ,s.start_date startdate,s.end_date enddate " +
                " FROM progress_report as pr  join semesters as s on s.semester_id=pr.semester_registration_id " +
                " WHERE pr.scholar_id = :scholarId " +
                " ORDER BY pr.insert_time DESC")
            .setParameter("scholarId", scholarId)
            .setMaxResults(1)
            .getResultList();

        ScholarDashboardDTO dashboard = new ScholarDashboardDTO();
        dashboard.setScholar(scholarDTO);

        if (!reportList.isEmpty()) {
            Object[] r = reportList.get(0);

            dashboard.setReportId(((Number) r[0]).intValue());
            dashboard.setSemesterRegistrationId(((Number) r[1]).intValue());
            dashboard.setProgressStatus((String) r[2]);
            dashboard.setNextActions((String) r[3]);
                      
            java.sql.Timestamp submittime = (Timestamp)r[4];
            String sumittime=Convert_timestamp_tohuman(submittime) ;
            dashboard.setSubmittedOn(sumittime);
            
            dashboard.setSemestername((String )r[5]);
            
            java.sql.Date sqlDate = (java.sql.Date) r[6];
            LocalDate startDate = sqlDate.toLocalDate();
             sqlDate = (java.sql.Date) r[7];
            LocalDate enddate = sqlDate.toLocalDate();
            
           
            dashboard.setStartdate(startDate);
            dashboard.setEnddate(enddate);
        
            
           

//            if (r[4] != null) {
//                dashboard.setSubmittedOn(
//                    new java.text.SimpleDateFormat("dd-MM-yyyy")
//                        .format((java.sql.Timestamp) r[4])
//                );
//            }
        }

        return dashboard;
    }
    
    public String  Convert_timestamp_tohuman(Timestamp timestamp) {
    

        // Convert to LocalDateTime
        java.time.LocalDateTime dateTime = timestamp.toLocalDateTime();

        // Format it
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern(
                        "d MMMM yyyy, hh:mm:ss a");

        String formattedDate = dateTime.format(formatter);

        System.out.println(formattedDate);
        return formattedDate;

    	
    }
    
    
    public Scholars getScholarByUserid(int userid) {
    	
    	return thescholarsRepository.findByUserId(userid)
    			.orElseThrow(() ->
                new RuntimeException("Scholar not found"));
    	
    	
    	
    	
    	
    }

}
