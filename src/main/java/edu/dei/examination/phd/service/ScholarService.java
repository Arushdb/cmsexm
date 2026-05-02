package edu.dei.examination.phd.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.model.UserIdentifier;
import edu.dei.examination.cmsexm.payload.request.UserDTO;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.service.UserService;
import edu.dei.examination.phd.dto.CreateScholarsRequest;
import edu.dei.examination.phd.dto.ScholarDTO;
import edu.dei.examination.phd.dto.ScholarDashboardDTO;
import edu.dei.examination.phd.enums.ProgressStatus;
import edu.dei.examination.phd.exception.ScholarValidationException;
import edu.dei.examination.phd.model.Department;
import edu.dei.examination.phd.model.Program;
import edu.dei.examination.phd.model.ProgramRoleAssignment;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.ScholarSemester;
import edu.dei.examination.phd.model.ScholarSupervisor;
import edu.dei.examination.phd.model.Scholars;
import edu.dei.examination.phd.model.Semesters;
import edu.dei.examination.phd.repository.DepartmentRepository;
import edu.dei.examination.phd.repository.ProgramRepository;
import edu.dei.examination.phd.repository.ProgramRoleAssignmentRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SemesterRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ScholarService {
	
	@Autowired
	ScholarsRepository c;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ScholarSemesterRepository theScholarSemesterRepository;
	@Autowired
	SemesterRepository theSemesterRepository;
	
	@Autowired
	UserService userservice;
	
	@Autowired
	UserRepository userRepo;

	@Autowired
	DepartmentRepository depRepo;

	@Autowired
	ProgramRepository pgmRepo;


	
	
	

    @Autowired private ScholarsRepository scholarRepo;
    @Autowired private ScholarSupervisorRepository supervisorRepo;
    @Autowired private ProgramRoleAssignmentRepository programRoleRepo;

	 @PersistenceContext(unitName = "phd") 
    private EntityManager em;

    @Transactional("phdTransactionManager")
    public int createScholarsForAcademicYearAndMonth(CreateScholarsRequest request) {
    	Object dbName = em
    		    .createNativeQuery("SELECT DATABASE()")
    		    .getSingleResult();
    		System.out.println("Connected DB = " + dbName);
    		
        String year = request.getAcademicYear().substring(0, 4);          
        List<ScholarDTO> list=em.createNamedQuery("Applicant.selectForScholar", ScholarDTO.class)
        		.setParameter("year", request.getAcademicYear())
        		.setParameter("month", request.getAdmissionMonth())
        		.getResultList();

        if (list.isEmpty()) {
            return 0;
        }

        String insertSql =
                "INSERT INTO scholars (program_id,  full_name, gender_id, email, date_of_birth,phone, category, admission_date, status_id, \r\n"
                + " created_at,application_number,department_id,user_id) " +
                "VALUES (:program_id, :first_name, :gender,:email,:dob,:phone,:category,:selection_date,1,now(),:appno,:department_id,:user_id)";

        LocalDateTime now = LocalDateTime.now();

        for (ScholarDTO row : list) {
            String appno =  row.getAppno();
            
   
        	   String username = row.getEmail();
        	   String password = row.getDateOfBirth().toString();
        	   Role scholarRole = roleRepository.findByName(ERole.ROLE_SCHOLAR)
        			    .orElseThrow(()->new RuntimeException("Scholar Role not found"));

        	   Integer roleId = scholarRole.getId();
        	   List <Integer>  listroleid= new ArrayList<>();
        	   listroleid.add(roleId);
        	   
        	   UserDTO userdto = new UserDTO();
        	   userdto.setUsername(username);
        	   userdto.setPassword(password);
        	   userdto.setRoleIds(listroleid);
        	  // User user=userservice.createUser(username, password,listroleid);
        	   
        	   User user=userservice.createUser(userdto);
        	   
        	    username = "SCH" + user.getId();
        	   user.setUsername(username);

        	   userRepo.save(user);  
        	   
        	  
        	   
        	   
        	        	   
        	   
        	   String idvalue = "APP"+year+row.getAppno();
        	   
        	   userservice.addUserIdentifier(
        			    user.getId().intValue(),
        			   //user.getUsername(),
        			    UserIdentifier.IdentifierType.APPLICATION_NO,
        			    idvalue
        			);
        	   
        	    em.createNativeQuery(insertSql)
                       .setParameter("appno", row.getAppno()) 
                       .setParameter("program_id", row.getProgramid())
                       .setParameter("first_name", row.getFullName())
                       .setParameter("gender", row.getGender_id())
                       .setParameter("email", row.getEmail()) 
                       .setParameter("dob", row.getDateOfBirth())
                       .setParameter("phone", row.getPhone())
                       .setParameter("category", row.getCategory())
                       .setParameter("selection_date", row.getAdmissionDate())
                       .setParameter("department_id", row.getDepartment_id())
                       .setParameter("user_id", user.getId())
                       		
                                 
                       .executeUpdate();
           
        }

        return list.size();
    }


    @Transactional("phdTransactionManager")
    public int createScholarsFromCMS(CreateScholarsRequest request) {
    	Object dbName = em
    		    .createNativeQuery("SELECT DATABASE()")
    		    .getSingleResult();
    		System.out.println("Connected DB = " + dbName);
    		
        String year = request.getAcademicYear().substring(0, 4);          
        List<ScholarDTO> list=em.createNamedQuery("Applicant.selectForScholar", ScholarDTO.class)
        		.setParameter("year", request.getAcademicYear())
        		.setParameter("month", request.getAdmissionMonth())
        		.getResultList();

        if (list.isEmpty()) {
            return 0;
        }

        String insertSql =
                "INSERT INTO scholars (program_id,  full_name, gender_id, email, date_of_birth,phone, category, admission_date, status_id, \r\n"
                + " created_at,application_number,department_id,user_id) " +
                "VALUES (:program_id, :first_name, :gender,:email,:dob,:phone,:category,:selection_date,1,now(),:appno,:department_id,:user_id)";

        LocalDateTime now = LocalDateTime.now();

        for (ScholarDTO row : list) {
            String appno =  row.getAppno();
            
   
        	   String username = row.getEmail();
        	   String password = row.getDateOfBirth().toString();
        	   Role scholarRole = roleRepository.findByName(ERole.ROLE_SCHOLAR)
        			    .orElseThrow(()->new RuntimeException("Scholar Role not found"));

        	   Integer roleId = scholarRole.getId();
        	   List <Integer>  listroleid= new ArrayList<>();
        	   listroleid.add(roleId);
        	   
        	   UserDTO userdto = new UserDTO();
        	   userdto.setUsername(username);
        	   userdto.setPassword(password);
        	   userdto.setRoleIds(listroleid);
        	  // User user=userservice.createUser(username, password,listroleid);
        	   
        	   User user=userservice.createUser(userdto);
        	   
        	    username = "SCH" + user.getId();
        	   user.setUsername(username);

        	   userRepo.save(user);  
        	   
        	  
        	   
        	   
        	        	   
        	   
        	   String idvalue = "APP"+year+row.getAppno();
        	   
        	   userservice.addUserIdentifier(
        			    user.getId().intValue(),
        			   //user.getUsername(),
        			    UserIdentifier.IdentifierType.APPLICATION_NO,
        			    idvalue
        			);
        	   
        	    em.createNativeQuery(insertSql)
                       .setParameter("appno", row.getAppno()) 
                       .setParameter("program_id", row.getProgramid())
                       .setParameter("first_name", row.getFullName())
                       .setParameter("gender", row.getGender_id())
                       .setParameter("email", row.getEmail()) 
                       .setParameter("dob", row.getDateOfBirth())
                       .setParameter("phone", row.getPhone())
                       .setParameter("category", row.getCategory())
                       .setParameter("selection_date", row.getAdmissionDate())
                       .setParameter("department_id", row.getDepartment_id())
                       .setParameter("user_id", user.getId())
                       		
                                 
                       .executeUpdate();
           
        }

        return list.size();
    }



	private String generateScholarNumber(Object[] row) {
        // row[2] = academic_year , row[0] = id  (index based on SELECT order)
        return "SCH-" + row[2] + "-" + row[0];
    }
    
    @Transactional(transactionManager = "phdTransactionManager")
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
        scholarDTO.setFullName((String) scholarRow[1]);
        scholarDTO.setCategory((String) scholarRow[2]);
        scholarDTO.setEmail((String) scholarRow[3]);
        scholarDTO.setPhone((String) scholarRow[4]);
        scholarDTO.setGender_id(((Number) scholarRow[5]).intValue());
        scholarDTO.setDateOfBirth(((java.sql.Date) scholarRow[6]).toLocalDate());
        scholarDTO.setProgramid(((Number) scholarRow[7]).intValue());
        scholarDTO.setAdmissionDate(((java.sql.Date) scholarRow[8]).toLocalDate());
        scholarDTO.setProgramname((String)scholarRow[9]);

        // ---------- 2. Fetch Latest Progress Report ----------
//        List<Object[]> reportList =  em.createNativeQuery(
//                " SELECT pr.id, pr.scholar_semester_id , pr.progress_status  , " +
//                " pr.next_actions, pr.submitted_at ,s.semester_name ,s.start_date startdate,s.end_date enddate, " +
//                " s.submission_deadline FROM progress_report as pr  join semesters as s on s.semester_id=pr.semester_registration_id " +
//                " WHERE pr.scholar_id = :scholarId  and  is_Active = '1' " +
//                " ORDER BY pr.insert_time DESC")
//            .setParameter("scholarId", scholarId)
//            .setMaxResults(1)
//            .getResultList();
        
        
        List<Object[]> reportList = em.createNativeQuery(
        	    " SELECT pr.id, pr.scholar_semester_id, pr.progress_status, " +
        	    " pr.next_actions, pr.submitted_at, s.semester_name, " +
        	    " s.start_date AS startdate, s.end_date AS enddate, " +
        	    " s.submission_deadline " +
        	    " FROM progress_report pr " +

        	    " JOIN scholar_semesters ss ON ss.id = pr.scholar_semester_id " +   // ✅ FIX
        	    " JOIN semesters s ON s.semester_id = ss.semester_id " +           // ✅ FIX

        	    " WHERE ss.scholar_id = :scholarId " +                             // ✅ BETTER
        	    " AND s.is_active = 1 " +                                         // ✅ FIX CASE

        	    " ORDER BY pr.insert_time DESC"
        	)
        	.setParameter("scholarId", scholarId)
        	.setMaxResults(1)
        	.getResultList(); 

        ScholarDashboardDTO dashboard = new ScholarDashboardDTO();
        dashboard.setScholar(scholarDTO);

        if (!reportList.isEmpty()) {
            Object[] r = reportList.get(0);

            dashboard.setReportId(((Number) r[0]).intValue());
            Integer scholarsemesterid = ((Number)r[1]).intValue();
            ScholarSemester ssm =theScholarSemesterRepository.findById(scholarsemesterid)
            		.orElseThrow(()->new RuntimeException("Scholar Semester record not found"));
            //dashboard.setSemesterRegistrationId(((Number) r[1]).intValue());
           dashboard.setSemesterRegistrationId(ssm.getSemester().getSemesterId());
            dashboard.setProgressStatus((String) r[2]);
            dashboard.setNextActions((String) r[3]);
                      
            java.sql.Timestamp submittime = (Timestamp)r[4];
            String sumittime="";
            if(submittime!=null)
            	sumittime=Convert_timestamp_tohuman(submittime) ;
            dashboard.setSubmittedOn(sumittime);
            
            dashboard.setSemestername((String )r[5]);
            
            java.sql.Date sqlDate = (java.sql.Date) r[6];
            LocalDate startDate = sqlDate.toLocalDate();
             sqlDate = (java.sql.Date) r[7];
            LocalDate enddate = sqlDate.toLocalDate();
            
            sqlDate = (java.sql.Date) r[8];
            LocalDate deadline = sqlDate.toLocalDate();
            
            if (deadline != null) {
                dashboard.setDeadline(deadline);
            }
            LocalDate today = LocalDate.now();
            
            boolean allowed = !today.isAfter(deadline);
         // check scholar extension
            if (!allowed) {

                List<Object> extension = em.createNativeQuery(
                        "SELECT extended_until " +
                        "FROM progress_report_extension " +
                        "WHERE scholar_id = :scholarId " +
                        "AND semester_id = :semesterId " +
                        "AND CURDATE() <= extended_until")
                    .setParameter("scholarId", scholarId)
                    .setParameter("semesterId", dashboard.getSemesterRegistrationId())
                    .setMaxResults(1)
                    .getResultList();

                if (!extension.isEmpty()) {
                    allowed = true;

                    java.sql.Date extDate = (java.sql.Date) extension.get(0);
                    dashboard.setExtensionDeadline(extDate.toLocalDate());
                }
            }

            dashboard.setSubmissionAllowed(allowed);

//            if (today.isAfter(deadline)) {
//                dashboard.setSubmissionAllowed(false);
//            } else {
//                dashboard.setSubmissionAllowed(true);
//            }
           
            dashboard.setStartdate(startDate);
            dashboard.setEnddate(enddate);
            
        
            
           

//            if (r[4] != null) {
//                dashboard.setSubmittedOn(
//                    new java.text.SimpleDateFormat("dd-MM-yyyy")
//                        .format((java.sql.Timestamp) r[4])
//                );
//            }
        }
        // if first time entry
        if (reportList.isEmpty()) {
        	
        	

            ProgressReport pr = new ProgressReport();
            //pr.setScholarId(scholarId);
//            ScholarSemester ssm=theScholarSemesterRepository
//            		.findTopByScholarScholarIdOrderBySemesterSemesterIdDesc(scholarId)
//            		.orElseThrow(()->new RuntimeException("Scholar semester not found")) ;
            ScholarSemester ssm=theScholarSemesterRepository.
            		findTopByScholarScholarIdAndReviewStatusNotOrderBySemesterSemesterIdDesc(scholarId, 
            				ScholarSemester.ReviewStatus.Approved)
            		.orElseThrow(()->new RuntimeException("Last semester  review is already approved .Valid Scholar semester not found")) ;
            		
            
         
            pr.setScholarSemester(ssm);
            pr.setProgressStatus(ProgressStatus.DRAFT);
            
           
//            ScholarSemester latestSemester = 
//            		theScholarSemesterRepository.findTopByScholarScholarIdOrderBySemesterSemesterIdDesc
//            		(scholarId).orElseThrow(()->new RuntimeException("Scholar semester not found"));
////            		findTopByScholarIdOrderBySemesterIdDesc(
//            		scholarId)
//    				.orElse(null);

//    		if (latestSemester != null) {
//    			if (latestSemester.getReviewStatus() != ScholarSemester.ReviewStatus.Approved) {
//
//    				pr.setSemesterRegistrationId(latestSemester.getSemester().getSemesterId());
//    				pr.setScholarSemester(latestSemester);
//
//    			}else {
//    				throw new ScholarValidationException("Last semester  review is already approved");
//    			}
//    		}
            
            
            		

            

            dashboard.setReportId(pr.getId());
            dashboard.setProgressStatus("DRAFT");
            dashboard.setSemesterRegistrationId(pr.getScholarSemester().getSemester().getSemesterId());
           
            Semesters semester= theSemesterRepository.
            		findById(pr.getScholarSemester().getSemester().getSemesterId()).orElse(null); 
            if(semester==null) {
            	throw new ScholarValidationException("Semester not available");
            }
            LocalDate today = LocalDate.now();
            Boolean allowed = !today.isAfter(semester.getSubmissiondeadline());
            dashboard.setSubmissionAllowed(allowed);
            
           
            dashboard.setSemestername(semester.getSemesterName());
            if (allowed)
            	em.persist(pr);
            
        }

        return dashboard;
    }
    
//    public boolean isProgressEntryAllowed(Date) {
//    	
//    }
    
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
    	
    	return scholarRepo.findByUserId(userid)
    			.orElseThrow(() ->
                new RuntimeException("Scholar not found"));
    	
    	
    	
    	
    	
    }
    
   


        // =========================
        // GET ALL
        // =========================
        public List<ScholarDTO> getAll() {
            return scholarRepo.findAll()
            		 .stream()
                     .map(s -> {
                         ScholarDTO dto = new ScholarDTO();
                         
                         dto.setScholarid(s.getScholarId());
                         dto.setFullName(s.getFullName());
                         dto.setEmail(s.getEmail());
                         dto.setEnrolmentno(s.getEnrolmentno());
                         dto.setProgramname(s.getProgram().getProgramname());
                         return dto;
                     })
                     .collect(Collectors.toList());
        }

        // =========================
        // GET BY ID
        // =========================
        public Scholars getById(Integer id) {
            return scholarRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Scholar not found"));
        }

        // =========================
        // UPDATE
        // =========================
        public Scholars update(Integer id, ScholarDTO updated) {

            Scholars s = getById(id);
            
            s.setFullName(updated.getFullName());
            s.setEmail(updated.getEmail());
            s.setPhone(updated.getPhone());
          
            s.setFathername(updated.getFathername());
            s.setAddressForCorrespondence(updated.getAddress());
            s.setAdmissionDate(updated.getAdmissionDate());
            s.setDateOfBirth(updated.getDateOfBirth());

            return scholarRepo.save(s);
        }

        // =========================
        // DELETE
        // =========================
        public void delete(Integer id) {
            scholarRepo.deleteById(id);
        }

        // =========================
        // GET SUPERVISOR
        // =========================
        public List<ScholarSupervisor> getSupervisor(Integer scholarId) {
            return supervisorRepo.findByScholar_ScholarIdAndIsActiveTrue(scholarId);
        }

        // =========================
        // GET PROGRAM ID
        // =========================
        private Integer getProgramId(Integer scholarId) {
            return getById(scholarId).getProgramId();
        }

        // =========================
        // GET HOD
        // =========================
        public List<ProgramRoleAssignment> getProgramByRole(Integer scholarId,String role) {
            //return programRoleRepo.findByProgram_IdAndRole(getProgramId(scholarId), "HOD");
           return  programRoleRepo.findByProgram_ProgramIdAndRole(getProgramId(scholarId), role);
        }
        
        
      

            @Transactional
            public ScholarDTO updateAcademic(Integer id, ScholarDTO dto) {

                Scholars s = scholarRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Scholar not found with id: " + id));

                // ✅ Safe updates
                if (dto.getRegistrationdate() != null) {
                    s.setRegistrationDate(dto.getRegistrationdate());
                }

                if (dto.getResearchtopiceng() != null) {
                    s.setResearchTopicEng(dto.getResearchtopiceng());
                }

                if (dto.getResearchtopichnd() != null) {
                    s.setResearchTopicHnd(dto.getResearchtopichnd());
                }

                if (dto.getDateextension() != null) {
                    s.setDateExtension(dto.getDateextension());
                }

                if (dto.getDateJRF() != null) {
                    s.setDateJrf(dto.getDateJRF());
                }

                if (dto.getDateJRFexp() != null) {
                    s.setDateJrfExp(dto.getDateJRFexp());
                }

                Scholars updated = scholarRepo.save(s);

                return map(updated); // your existing mapper
            }
            
            private ScholarDTO map(Scholars s) {
                ScholarDTO dto = new ScholarDTO();

                dto.setScholarid(s.getScholarId());
                dto.setRegistrationdate(s.getRegistrationDate());
                dto.setResearchtopiceng(s.getResearchTopicEng());
                dto.setResearchtopichnd(s.getResearchTopicHnd());
                dto.setDateextension(s.getDateExtension());
                dto.setDateJRF(s.getDateJrf());
                dto.setDateJRFexp(s.getDateJrfExp());

                return dto;
            }
            
            @Transactional
            public Page<ScholarDTO> search(Pageable pageable) {

                Page<Scholars> page = scholarRepo.findAll(pageable);

                return page.map(this::mapToDTO);
            }
            @Transactional
            public Page<ScholarDTO> search(Pageable pageable,String keyword, Integer deptId) {

            	 Page<Scholars> page = scholarRepo.search(keyword, deptId,pageable);
            	 return page.map(this::mapToDTO);
                
            }
       
           
            
            private ScholarDTO mapToDTO(Scholars s) {
                return new ScholarDTO(
                        s.getScholarId(),
                        s.getFullName(),
                        s.getEnrolmentno(),
                        s.getDepartment().getDepartmentName(),
                        s.getProgram().getProgramname(),
                        s.getEmail(),
                        s.getPhone(),
                        s.getFathername(),
                        s.getAddressForCorrespondence(),
                        s.getAdmissionDate(),
                        s.getDateOfBirth(),
                        s.getResearchTopicEng(),
                        s.getResearchTopicHnd(),
                        s.getNameInHindi(),
                        s.getDateJrf(),
                        s.getDateJrfExp(),
                        s.getDateExtension(),
                        s.getRegistrationDate()
                );
            }
            
            
            @Transactional
            public void importScholars(MultipartFile file) throws Exception {

                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                	
                	

                    Row row = sheet.getRow(i);
                    if (row == null) continue;
                    

                    // ❌ Skip garbage rows like "FACULTY OF EDUCATION"
                    Cell firstCell = row.getCell(0);
                    if (firstCell == null || firstCell.getCellType() == CellType.BLANK) continue;

                    String name = getString(row.getCell(1));
                    if (name == null || name.trim().isEmpty()) continue;

                    Scholars s = new Scholars();
                    
                    String email =getString(row.getCell(6));
                    if (scholarRepo.findByEmail(email).isPresent())
                    	continue;

                    // ✔ Map only required columns (ignore rest)
                    s.setFullName(name);
                    s.setNameInHindi(getString(row.getCell(2)));
                    
                    s.setFathername(getString(row.getCell(3)));
                    
                    s.setMothername(getString(row.getCell(4)));
                   
                    

                    // ✔ Date handling
                    s.setDateOfBirth(getDate(row.getCell(5)));
                    s.setEmail(getString(row.getCell(6)));
                    s.setPhone(getString(row.getCell(7)));
                    
                    
                    String[] parts= getString(row.getCell(8)).split("\\r?\\n");
                    String admission = parts.length > 0 ? parts[0].trim() : null;
                    
                   
                     admission = clean(admission);
                    
                    
                    
                    admission=	extractDate(clean(admission));
                    s.setAdmissionDate(parseMonthYear(admission));
                    if (s.getAdmissionDate()==null)
                    	s.setAdmissionDate(parseDayMonthYear(admission)); 
                    
                    admission=  extractDate(clean(getString(row.getCell(9))));
                    
                    s.setRegistrationDate(parseDayMonthYear(admission)); 
                   
                    
                    //s.setSupervisor(getString(row.getCell(10)));
                    //s.setCoSupervisor(getString(row.getCell(11)));

                    s.setThesisTitle(getString(row.getCell(12)));
                    String deptname =getString(row.getCell(13));
                     parts= (deptname).split("\\r?\\n");
                     deptname = parts.length > 0 ? parts[0].trim() : null;
                     
                    Department dept =depRepo.findByDepartmentName(deptname).orElseThrow(()->new Exception("Department not exists"));
                    List<Program> pgm = pgmRepo.findByDepartmentid(dept.getDepartmentId())
                    		.orElseThrow(()->new RuntimeException("Program not found")); 
                    s.setProgram(pgm.get(0));
                    s.setSubject(getString(row.getCell(13)));
                    s.setDepartment(dept); 
                     parts= getString(row.getCell(14)).split("\\r?\\n");
                    String enrolmentNo = parts.length > 0 ? parts[0].trim() : null;
                    	//	String[] parts = cellValue.split("\\r?\\n");
                    
                    if( isNumeric(enrolmentNo))
                         s.setEnrolmentno(enrolmentNo);
                    else
                    	s.setEnrolmentno(null);
              
                    //s.setEnrolmentNumber(getString(row.getCell(14)));
                    
                    String gender =getString(row.getCell(15));
                    if (gender.equalsIgnoreCase("M"))
                    s.setGenderId(1);
                    else
                    	s.setGenderId(2);	
                    	
                    s.setCategory(getString(row.getCell(16)));
                    s.setStatus(getString(row.getCell(17)));
                    s.setState(getString(row.getCell(18)));
                    
                    s.setMinority(getString(row.getCell(19)));
                   
                    //(getString(row.getCell(17)));

                    scholarRepo.save(s);
                }

                workbook.close();
            }  
            
            private boolean isNumeric(String value) {
                if (value == null) return false;

                value = value.trim();

                return value.matches("\\d+");
            }
            
            private String extractDate(String value) {
                if (value == null) return null;
                
                String cleaned = value.trim()
                        .replace("\u00A0", " ")
                        .replaceAll("\\s+", " ")
                        .toUpperCase();

                Pattern pattern = Pattern.compile("\\d{2}-[A-Z]{3}-\\d{4}");
                Matcher matcher = pattern.matcher(value.toUpperCase());

                if (matcher.find()) {
                    return matcher.group(); // returns "14-DEC-2020"
                }
                
                // 2️⃣ Month-year pattern: SEP-2023
                Pattern monthYear = Pattern.compile("\\b[A-Z]{3}-\\d{4}\\b");
                Matcher m2 = monthYear.matcher(cleaned);
                if (m2.find()) {
                    return m2.group();
                }

                return null;
            }
            
            private LocalDate parseMonthYear(String raw) {
                if (raw == null || raw.isEmpty()) return null;
                
//                System.out.println("RAW = [" + raw + "]");
//                for (char c : raw.toCharArray()) {
//                    System.out.println("CHAR: [" + c + "] ASCII: " + (int) c);
//                }
                
                String value = normalize(raw).trim();

                DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
                
                DateTimeFormatter formatter =
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()   // ⭐ CRITICAL FIX
                                .appendPattern("dd-MMM-yyyy")
                                .toFormatter(Locale.ENGLISH);
                LocalDate  dt=null;
                try {
                	if (value.length()<10)
                	   dt = LocalDate.parse("01-" + value, formatter);
                	else
                		 dt = LocalDate.parse(value,format1);
                	 return dt;
//                YearMonth yearMonth = YearMonth.parse(value, formatter);
//                return yearMonth.atDay(1); // default day = 1
                }
                catch(Exception e) {
                	e.printStackTrace();
                	return null;
                }

               
            }
            
            private LocalDate parseDayMonthYear(String raw) {
                if (raw == null || raw.isEmpty()) return null;
                
//                System.out.println("RAW = [" + raw + "]");
//                for (char c : raw.toCharArray()) {
//                    System.out.println("CHAR: [" + c + "] ASCII: " + (int) c);
//                }
                
                String value = normalize(raw).trim();

               //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
                
                DateTimeFormatter formatter =
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()   // ⭐ CRITICAL FIX
                                .appendPattern("dd-MMM-yyyy")
                                .toFormatter(Locale.ENGLISH);
                try {
                	
                	 return LocalDate.parse( value, formatter);
//                YearMonth yearMonth = YearMonth.parse(value, formatter);
//                return yearMonth.atDay(1); // default day = 1
                }
                catch(Exception e) {
                	e.printStackTrace();
                	return null;
                }

               
            }
            
            private String normalize(String value) {
                if (value == null) return null;

                return value.trim()
                        .replace('\u00A0', ' ')   // non-breaking space
                        .replace('–', '-')   
                        .replaceAll("[^\\x00-\\x7F]", "")// special dash → normal dash
                        .replace("\n", "")
                        .replace("\r", "")
                        .toUpperCase();
            }
            
            private String clean(String value) {
                if (value == null) return null;

                return value.trim()
                        .replace("'", "")        // remove leading quote
                        .replace("\"", "")       // remove double quote
                        .replace("\u00A0", "");  // remove hidden space
            }
            
            
            private String getString(Cell cell) {
                if (cell == null) return null;
                cell.setCellType(CellType.STRING);
                return cell.getStringCellValue().trim();
            }

            private LocalDate getDate(Cell cell) {
                if (cell == null) return null;

                if (cell.getCellType() == CellType.NUMERIC) {
                    return cell.getLocalDateTimeCellValue().toLocalDate();
                }

                return null;
            }
            
//            private void  createuser(Scholars sch) {
//            	
//            
//            String username = sch.getEmail();
//     	   String password = sch.getDateOfBirth().toString();
//     	   Role scholarRole = roleRepository.findByName(ERole.ROLE_SCHOLAR)
//     			    .orElseThrow(()->new RuntimeException("Scholar Role not found"));
//
//     	   Integer roleId = scholarRole.getId();
//     	   List <Integer>  listroleid= new ArrayList<>();
//     	   listroleid.add(roleId);
//     	   
//     	   UserDTO userdto = new UserDTO();
//     	   userdto.setUsername(username);
//     	   userdto.setPassword(password);
//     	   userdto.setRoleIds(listroleid);
//     	  // User user=userservice.createUser(username, password,listroleid);
//     	   
//     	   User user=userservice.createUser(userdto);
//     	   
//     	    username = "SCH" + user.getId();
//     	   user.setUsername(username);
//
//     	   userRepo.save(user);  
//     	     	        	   
//     	   
//     	 //  String idvalue = "APP"+year+row.getAppno();
//     	   
//     	   userservice.addUserIdentifier(
//     			    user.getId().intValue(),
//     			   //user.getUsername(),
//     			    UserIdentifier.IdentifierType.APPLICATION_NO,
//     			    123
//     			);
//            }
//        
} 

        
    

