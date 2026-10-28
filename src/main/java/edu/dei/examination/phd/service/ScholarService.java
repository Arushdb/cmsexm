package edu.dei.examination.phd.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.dei.examination.cmsexm.model.ERole;
import edu.dei.examination.cmsexm.model.Role;
import edu.dei.examination.cmsexm.model.User;
import edu.dei.examination.cmsexm.model.UserIdentifier;
import edu.dei.examination.cmsexm.model.UserIdentifier.IdentifierType;
import edu.dei.examination.cmsexm.payload.request.UserDTO;
import edu.dei.examination.cmsexm.repository.RoleRepository;
import edu.dei.examination.cmsexm.repository.UserIdentifierRepository;
import edu.dei.examination.cmsexm.repository.UserRepository;
import edu.dei.examination.cmsexm.service.PasswordService;
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
import edu.dei.examination.phd.model.SubjectProgramMapping;
import edu.dei.examination.phd.repository.DepartmentRepository;
import edu.dei.examination.phd.repository.ProgramRepository;
import edu.dei.examination.phd.repository.ProgramRoleAssignmentRepository;
import edu.dei.examination.phd.repository.ScholarSemesterRepository;
import edu.dei.examination.phd.repository.ScholarSupervisorRepository;
import edu.dei.examination.phd.repository.ScholarsRepository;
import edu.dei.examination.phd.repository.SemesterRepository;
import edu.dei.examination.phd.repository.SubjectProgramMappingRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.HashSet;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	UserIdentifierRepository userIdentifierRepo ;

	@Autowired
	DepartmentRepository depRepo;
	@Autowired
	PasswordService passwordService ;
	
	

	@Autowired
	ProgramRepository pgmRepo;

	@Autowired
	private ScholarsRepository scholarRepo;
	@Autowired
	private ScholarSupervisorRepository supervisorRepo;
	@Autowired
	private ProgramRoleAssignmentRepository programRoleRepo;
	@Autowired
	private SubjectProgramMappingRepository mappingRepository;

	@PersistenceContext(unitName = "phd")
	private EntityManager em;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Transactional("phdTransactionManager")
	public int createScholarsForAcademicYearAndMonth(CreateScholarsRequest request) {
		Object dbName = em.createNativeQuery("SELECT DATABASE()").getSingleResult();
		System.out.println("Connected DB = " + dbName);

		String year = request.getAcademicYear().substring(0, 4);
		System.out.println(request.getAcademicYear());

		Integer month = request.getAdmissionMonth();

		if (month == null || month < 1 || month > 12) {
			throw new IllegalArgumentException("Admission month must be between 1 and 12");
		}

		//String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

		//String monthName = months[month - 1];

		List<ScholarDTO> list = em.createNamedQuery("Applicant.selectForScholar", ScholarDTO.class)
				.setParameter("year", request.getAcademicYear()).setParameter("month", request.getAdmissionMonth())
				.getResultList();

		if (list.isEmpty()) {
			return 0;
		}

		String insertSql = "INSERT INTO scholars (program_id,  full_name, gender_id, email, date_of_birth,phone, category, admission_date, status_id, \r\n"
				+ " createdAt,application_number,department_id,subject,mode) "
				+ "VALUES (:program_id, :first_name, :gender,:email,:dob,:phone,:category,:selection_date,1,now(),:appno,:department_id,:subject,:mode)";

		//LocalDateTime now = LocalDateTime.now();

		for (ScholarDTO row : list) {
			
			
			if (scholarRepo.findByEmail(row.getEmail()).isPresent())
				continue;
			
			String appno = row.getAppno();
			
			String subjectKey = normalizeSubject(row.getSubject());

			SubjectProgramMapping mapping =
			    mappingRepository
			        .findBySubjectKeyAndActiveTrue(subjectKey)
			        .orElseThrow(() -> new IllegalStateException(
			            "No department/program mapping found for subject: "
			            + subjectKey
			        ));

			Long departmentId = mapping.getDepartmentId();
			Long programId = mapping.getProgramId();

			// Assign these IDs to the scholar entity
			// using your existing entity fields and relationships.

			
			em.createNativeQuery(insertSql).setParameter("appno", appno)

					.setParameter("first_name", row.getFullName()).setParameter("gender", row.getGender_id())
					.setParameter("email", row.getEmail()).setParameter("dob", row.getDateOfBirth())
					.setParameter("phone", row.getPhone()).setParameter("category", row.getCategory())
					.setParameter("selection_date", row.getAdmissionDate())
					.setParameter("department_id", departmentId)
					.setParameter("program_id", programId)
					
					
					.setParameter("subject", row.getSubject()).setParameter("mode", row.getMode())

					.executeUpdate();

		}

		return list.size();
	}

		private String normalizeSubject(String subject) {

	    if (subject == null || subject.trim().isEmpty()) {
	        throw new IllegalArgumentException(
	            "Subject is missing from CMS"
	        );
	    }

	    return subject.trim()
	            .replaceAll("\\s+", " ")
	            .toUpperCase(Locale.ROOT);
	}
	private String generateScholarNumber(Object[] row) {
		// row[2] = academic_year , row[0] = id (index based on SELECT order)
		return "SCH-" + row[2] + "-" + row[0];
	}

	@Transactional(transactionManager = "phdTransactionManager")
	public ScholarDashboardDTO getScholarDashboard(int scholarId) {

		// ---------- 1. Fetch Scholar Info ----------
		Object[] scholarRow = (Object[]) em.createNativeQuery("SELECT s.application_number, s.full_name, s.category, "
				+ "       s.email, s.phone, s.gender_id, s.date_of_birth, "
				+ "       s.program_id, s.admission_date ,p.name as programname" +

				" FROM scholars s  join programs p on p.program_id=s.program_id" + " WHERE s.scholar_id = :scholarId")
				.setParameter("scholarId", scholarId).getSingleResult();

		ScholarDTO scholarDTO = new ScholarDTO();
		scholarDTO.setAppno((String) scholarRow[0]);
		scholarDTO.setFullName((String) scholarRow[1]);
		scholarDTO.setCategory((String) scholarRow[2]);
		scholarDTO.setEmail((String) scholarRow[3]);
		scholarDTO.setPhone((String) scholarRow[4]);
		scholarDTO.setGender_id(((Number) scholarRow[5]).intValue());
		scholarDTO.setDateOfBirth(((java.sql.Date) scholarRow[6]).toLocalDate());

		scholarDTO.setAdmissionDate(((java.sql.Date) scholarRow[8]).toLocalDate());
		scholarDTO.setProgramname((String) scholarRow[9]);

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

		List<Object[]> reportList = em.createNativeQuery(" SELECT pr.id, pr.scholar_semester_id, pr.progress_status, "
				+ " pr.next_actions, pr.submitted_at, s.semester_name, "
				+ " s.start_date AS startdate, s.end_date AS enddate, " + " s.submission_deadline "
				+ " FROM progress_report pr " +

				" JOIN scholar_semesters ss ON ss.id = pr.scholar_semester_id " + // ✅ FIX
				" JOIN semesters s ON s.semester_id = ss.semester_id " + // ✅ FIX

				" WHERE ss.scholar_id = :scholarId " + // ✅ BETTER
				" AND s.is_active = 1 " + // ✅ FIX CASE

				" ORDER BY pr.insert_time DESC").setParameter("scholarId", scholarId).setMaxResults(1).getResultList();

		ScholarDashboardDTO dashboard = new ScholarDashboardDTO();
		dashboard.setScholar(scholarDTO);

		if (!reportList.isEmpty()) {
			Object[] r = reportList.get(0);

			dashboard.setReportId(((Number) r[0]).intValue());
			Integer scholarsemesterid = ((Number) r[1]).intValue();
			ScholarSemester ssm = theScholarSemesterRepository.findById(scholarsemesterid)
					.orElseThrow(() -> new RuntimeException("Scholar Semester record not found"));
			// dashboard.setSemesterRegistrationId(((Number) r[1]).intValue());
			dashboard.setSemesterRegistrationId(ssm.getSemester().getSemesterId());
			dashboard.setProgressStatus((String) r[2]);
			dashboard.setNextActions((String) r[3]);

			java.sql.Timestamp submittime = (Timestamp) r[4];
			String sumittime = "";
			if (submittime != null)
				sumittime = Convert_timestamp_tohuman(submittime);
			dashboard.setSubmittedOn(sumittime);

			dashboard.setSemestername((String) r[5]);

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

				List<Object> extension = em
						.createNativeQuery("SELECT extended_until " + "FROM progress_report_extension "
								+ "WHERE scholar_id = :scholarId " + "AND semester_id = :semesterId "
								+ "AND CURDATE() <= extended_until")
						.setParameter("scholarId", scholarId)
						.setParameter("semesterId", dashboard.getSemesterRegistrationId()).setMaxResults(1)
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
			// pr.setScholarId(scholarId);
//            ScholarSemester ssm=theScholarSemesterRepository
//            		.findTopByScholarScholarIdOrderBySemesterSemesterIdDesc(scholarId)
//            		.orElseThrow(()->new RuntimeException("Scholar semester not found")) ;
			ScholarSemester ssm = theScholarSemesterRepository
					.findTopByScholarScholarIdAndReviewStatusNotOrderBySemesterSemesterIdDesc(scholarId,
							ScholarSemester.ReviewStatus.Approved)
					.orElseThrow(() -> new RuntimeException(
							"Last semester  review is already approved .Valid Scholar semester not found"));

			pr.setScholarSemester(ssm);
			pr.setProgressStatus(ProgressStatus.DRAFT);

			dashboard.setReportId(pr.getId());
			dashboard.setProgressStatus("DRAFT");
			dashboard.setSemesterRegistrationId(pr.getScholarSemester().getSemester().getSemesterId());

			Semesters semester = theSemesterRepository.findById(pr.getScholarSemester().getSemester().getSemesterId())
					.orElse(null);
			if (semester == null) {
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

	public String Convert_timestamp_tohuman(Timestamp timestamp) {

		// Convert to LocalDateTime
		java.time.LocalDateTime dateTime = timestamp.toLocalDateTime();

		// Format it
		java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
				.ofPattern("d MMMM yyyy, hh:mm:ss a");

		String formattedDate = dateTime.format(formatter);

		System.out.println(formattedDate);
		return formattedDate;

	}

	public Scholars getScholarByUserid(int userid) {

		return scholarRepo.findByUserId(userid).orElseThrow(() -> new RuntimeException("Scholar not found"));

	}

	// =========================
	// GET ALL
	// =========================
	public List<ScholarDTO> getAll() {
		return scholarRepo.findAll().stream().map(s -> {
			ScholarDTO dto = new ScholarDTO();

			dto.setScholarid(s.getScholarId());
			dto.setFullName(s.getFullName());
			dto.setEmail(s.getEmail());
			dto.setEnrolmentno(s.getEnrolmentno());
			dto.setProgramname(s.getProgram().getProgramname());
			return dto;
		}).collect(Collectors.toList());
	}

	// =========================
	// GET BY ID
	// =========================
	public Scholars getById(Integer id) {
		return scholarRepo.findById(id).orElseThrow(() -> new RuntimeException("Scholar not found"));
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
	public List<ProgramRoleAssignment> getProgramByRole(Integer scholarId, String role) {
		// return programRoleRepo.findByProgram_IdAndRole(getProgramId(scholarId),
		// "HOD");
		return programRoleRepo.findByProgram_ProgramIdAndRole(getProgramId(scholarId), role);
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
	public Page<ScholarDTO> search(Pageable pageable, String keyword, Integer deptId) {

		Page<Scholars> page = scholarRepo.search(keyword, deptId, pageable);
		return page.map(this::mapToDTO);

	}

	private ScholarDTO mapToDTO(Scholars s) {
		return new ScholarDTO(s.getScholarId(), s.getFullName(), s.getEnrolmentno(),
				s.getDepartment().getDepartmentName(), s.getProgram().getProgramname(), s.getEmail(), s.getPhone(),
				s.getFathername(), s.getAddressForCorrespondence(), s.getAdmissionDate(), s.getDateOfBirth(),
				s.getResearchTopicEng(), s.getResearchTopicHnd(), s.getNameInHindi(), s.getDateJrf(), s.getDateJrfExp(),
				s.getDateExtension(), s.getRegistrationDate(),s.getUserId());
	}

	@Transactional(rollbackFor = Exception.class)
	public void importScholars(MultipartFile file) throws Exception {

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				try {

					Row row = sheet.getRow(i);
					if (row == null)
						continue;

					// ❌ Skip garbage rows like "FACULTY OF EDUCATION"
					Cell firstCell = row.getCell(0);
					if (firstCell == null || firstCell.getCellType() == CellType.BLANK)
						continue;

					String name = getString(row.getCell(1));
					if (name == null || name.trim().isEmpty())
						continue;

					Scholars s = new Scholars();

					String email = getString(row.getCell(6));
					if (scholarRepo.findByEmail(email).isPresent())
						continue;
					
					System.out.println("Processing Excel row: " + (i + 1));

					// ✔ Map only required columns (ignore rest)
					s.setFullName(name);
					s.setNameInHindi(getString(row.getCell(2)));

					s.setFathername(getString(row.getCell(3)));

					s.setMothername(getString(row.getCell(4)));

					// ✔ Date handling
					s.setDateOfBirth(getDate(row.getCell(5)));
					s.setEmail(getString(row.getCell(6)));
					s.setPhone(getString(row.getCell(7)));

					String[] parts = getString(row.getCell(8)).split("\\r?\\n");
					String admission = parts.length > 0 ? parts[0].trim() : null;

					admission = clean(admission);

					admission = extractDate(clean(admission));
					s.setAdmissionDate(parseMonthYear(admission));
					if (s.getAdmissionDate() == null)
						s.setAdmissionDate(parseDayMonthYear(admission));

					admission = extractDate(clean(getString(row.getCell(9))));

					s.setRegistrationDate(parseDayMonthYear(admission));

					// s.setSupervisor(getString(row.getCell(10)));
					// s.setCoSupervisor(getString(row.getCell(11)));

					s.setThesisTitle(getString(row.getCell(12)));
					String deptname = getString(row.getCell(13));
					parts = (deptname).split("\\r?\\n");
					deptname = parts.length > 0 ? parts[0].trim() : null;

					Department dept = depRepo.findByDepartmentName(deptname)
							.orElseThrow(() -> new Exception("Department not exists"));
					List<Program> pgm = pgmRepo.findByDepartmentid(dept.getDepartmentId())
							.orElseThrow(() -> new RuntimeException("Program not found"));
					s.setProgram(pgm.get(0));
					s.setSubject(getString(row.getCell(13)));
					s.setDepartment(dept);
					parts = getString(row.getCell(14)).split("\\r?\\n");
					String enrolmentNo = parts.length > 0 ? parts[0].trim() : null;
					// String[] parts = cellValue.split("\\r?\\n");

					if (isNumeric(enrolmentNo))
						s.setEnrolmentno(enrolmentNo);
					else
						s.setEnrolmentno(null);

					// s.setEnrolmentNumber(getString(row.getCell(14)));

					String gender = getString(row.getCell(15));
					if (gender.equalsIgnoreCase("M"))
						s.setGenderId(1);
					else
						s.setGenderId(2);

					s.setCategory(getString(row.getCell(16)));
					s.setStatus(getString(row.getCell(17)));
					s.setState(getString(row.getCell(18)));

					s.setMinority(getString(row.getCell(19)));

					// (getString(row.getCell(17)));

//                    Role scholarRole = roleRepository.findByName(ERole.ROLE_SCHOLAR)
//            			    .orElseThrow(()->new RuntimeException("Scholar Role not found"));
//
//            	   Integer roleId = scholarRole.getId();
//            	   List <Integer>  listroleid= new ArrayList<>();
//            	   listroleid.add(roleId);
//            	   
//            	   UserDTO userdto = new UserDTO();
//            	   userdto.setUsername(s.getEnrolmentno());
//            	   userdto.setPassword(s.getDateOfBirth().toString());
//            	   userdto.setRoleIds(listroleid);
//            	   userdto.setEmail(s.getEmail());
//            	   userdto.setName(s.getFullName());
//            	   userdto.setPhone(s.getPhone());

					// User user=userservice.createUser(username, password,listroleid);

					scholarRepo.save(s);
					System.out.println("Saved Excel row: " + (i + 1));
//            	   User user=userservice.createUser(userdto);
//            	   Scholars sch=scholarRepo.findByEmail(email).orElseThrow(()->
//            			   new RuntimeException("Scholar does not exists")
//            			   );
//            			   

//            	   sch.setUserId(user.getId());
//            	   scholarRepo.save(sch);

				} catch (Exception ex) {
					System.err.println("Error at Excel row: " + (i + 1));
					ex.printStackTrace();
					throw ex;
				}
			}
		}

	}

	private boolean isNumeric(String value) {
		if (value == null)
			return false;

		value = value.trim();

		return value.matches("\\d+");
	}

	private String extractDate(String value) {
		if (value == null)
			return null;

		String cleaned = value.trim().replace("\u00A0", " ").replaceAll("\\s+", " ").toUpperCase();

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
		if (raw == null || raw.isEmpty())
			return null;

//                System.out.println("RAW = [" + raw + "]");
//                for (char c : raw.toCharArray()) {
//                    System.out.println("CHAR: [" + c + "] ASCII: " + (int) c);
//                }

		String value = normalize(raw).trim();

		DateTimeFormatter format1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive() // ⭐ CRITICAL FIX
				.appendPattern("dd-MMM-yyyy").toFormatter(Locale.ENGLISH);
		LocalDate dt = null;
		try {
			if (value.length() < 10)
				dt = LocalDate.parse("01-" + value, formatter);
			else
				dt = LocalDate.parse(value, format1);
			return dt;
//                YearMonth yearMonth = YearMonth.parse(value, formatter);
//                return yearMonth.atDay(1); // default day = 1
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private LocalDate parseDayMonthYear(String raw) {
		if (raw == null || raw.isEmpty())
			return null;

//                System.out.println("RAW = [" + raw + "]");
//                for (char c : raw.toCharArray()) {
//                    System.out.println("CHAR: [" + c + "] ASCII: " + (int) c);
//                }

		String value = normalize(raw).trim();

		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy",
		// Locale.ENGLISH);

		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive() // ⭐ CRITICAL FIX
				.appendPattern("dd-MMM-yyyy").toFormatter(Locale.ENGLISH);
		try {

			return LocalDate.parse(value, formatter);
//                YearMonth yearMonth = YearMonth.parse(value, formatter);
//                return yearMonth.atDay(1); // default day = 1
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private String normalize(String value) {
		if (value == null)
			return null;

		return value.trim().replace('\u00A0', ' ') // non-breaking space
				.replace('–', '-').replaceAll("[^\\x00-\\x7F]", "")// special dash → normal dash
				.replace("\n", "").replace("\r", "").toUpperCase();
	}

	private String clean(String value) {
		if (value == null)
			return null;

		return value.trim().replace("'", "") // remove leading quote
				.replace("\"", "") // remove double quote
				.replace("\u00A0", ""); // remove hidden space
	}

	private String getString(Cell cell) {
		if (cell == null)
			return null;
		cell.setCellType(CellType.STRING);
		return cell.getStringCellValue().trim();
	}

	private LocalDate getDate(Cell cell) {
		if (cell == null)
			return null;

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

	@Transactional
	public int createScholarsFromCMS() {

		List<ScholarDTO> applicants = em.createNamedQuery("Applicant.getCMSScholars", ScholarDTO.class).getResultList();

		if (applicants.isEmpty()) {
			return 0;
		}

		Role scholarRole = roleRepository.findByName(ERole.ROLE_SCHOLAR)
				.orElseThrow(() -> new IllegalStateException("Scholar role not found"));

		String insertSql = "INSERT INTO scholars (" + " program_id, full_name, gender_id, email, date_of_birth, phone, "
				+ " category, admission_date, status_id, created_at, " + " application_number, department_id, user_id )"
				+

				" VALUES ( " + "    :program_id, :full_name, :gender_id, :email, :dob, :phone, "
				+ "    :category, :admission_date, 1, NOW(), " + "    :application_number, :department_id, :user_id "

		;

		int importedCount = 0;

		for (ScholarDTO row : applicants) {
			// Skip incomplete source records.
			if (row == null || row.getAppno() == null || row.getAppno().isEmpty() || row.getEmail() == null
					|| row.getEmail().isEmpty() || row.getDateOfBirth() == null

					|| row.getDepartment_id() == null) {
				continue;
			}

			String applicationNumber = row.getAppno().trim();

			// Prevent importing the same CMS application more than once.
			boolean alreadyImported = !em
					.createNativeQuery(" SELECT 1 FROM scholars WHERE application_number = :applicationNumber")

					.setParameter("applicationNumber", applicationNumber).getResultList().isEmpty();

			if (alreadyImported) {
				continue;
			}

			UserDTO userDto = new UserDTO();
			userDto.setUsername(row.getEmail().trim());
			userDto.setPassword(row.getDateOfBirth().toString());
			List<Integer> roleIds = new ArrayList<>();
			roleIds.add(scholarRole.getId());
			userDto.setRoleIds(roleIds);
			// userDto.setRoleIds(List.of(scholarRole.getId()));

			User user = userservice.createUser(userDto);

			user.setUsername("SCH" + user.getId());
			userRepo.save(user);

			String identifier = "APP" + "2026" + applicationNumber;

			userservice.addUserIdentifier(user.getId().intValue(), UserIdentifier.IdentifierType.APPLICATION_NO,
					identifier);

			em.createNativeQuery(insertSql)

					.setParameter("full_name", row.getFullName()).setParameter("gender_id", row.getGender_id())
					.setParameter("email", row.getEmail()).setParameter("dob", row.getDateOfBirth())
					.setParameter("phone", row.getPhone()).setParameter("category", row.getCategory())
					.setParameter("admission_date", row.getAdmissionDate())
					.setParameter("application_number", applicationNumber)
					.setParameter("department_id", row.getDepartment_id()).setParameter("user_id", user.getId())
					.executeUpdate();

			importedCount++;
		}

		return importedCount;
	}

	private final SecureRandom random = new SecureRandom();

//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public CreatedAccount createUserForScholar(Integer scholarId) {
//
//		Scholars scholar = scholarRepo.findById(scholarId)
//				.orElseThrow(() -> new RuntimeException("Scholar not found: " + scholarId));
//
//		if (scholar.getUserId() != null) {
//			throw new RuntimeException("Scholar already has a user account: " + scholarId);
//		}
//
//		String username = scholar.getEnrolmentno();
//
//		if (username == null || username.trim().isEmpty()) {
//			throw new RuntimeException("Enrolment number is missing for scholar " + scholarId);
//		}
//
//		if (userRepo.existsByUsername(username)) {
//			throw new RuntimeException("Username already exists: " + username);
//		}
//
//		if (scholar.getEmail() == null || scholar.getEmail().trim().isEmpty()) {
//			throw new RuntimeException("Email is missing for scholar " + scholarId);
//		}
//
//		String temporaryPassword = generateTemporaryPassword();
//
//		User user = new User();
//		user.setUsername(username);
//		user.setPassword(passwordEncoder.encode(temporaryPassword));
//		user.setEmail(scholar.getEmail());
//		user.setName(scholar.getFullName());
//		user.setPhone(scholar.getPhone());
//
//		// Replace this with the actual ID of your SCHOLAR role.
//		Integer scholarRoleId = 4;
//
////                List<Role> rolesFound =
////                    roleRepository.findAllById(List.of(scholarRoleId));
//
//		List<Role> rolesFound = roleRepository.findAllById(java.util.Collections.singletonList(scholarRoleId));
//
//		if (rolesFound.isEmpty()) {
//			throw new RuntimeException("Scholar role not found");
//		}
//
//		user.setRoles(new HashSet<>(rolesFound));
//
//		User savedUser = userRepo.save(user);
//
//		scholar.setUserId(savedUser.getId());
//		scholarRepo.save(scholar);
//
//		CreatedAccount result = new CreatedAccount();
//		result.scholarId = scholar.getScholarId();
//		result.scholarName = scholar.getFullName();
//		result.username = username;
//		result.email = scholar.getEmail();
//		result.temporaryPassword = temporaryPassword;
//		result.status = "CREATED";
//
//		return result;
//	}
//
	private String generateTemporaryPassword() {
		String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789@#$!";

		StringBuilder password = new StringBuilder();

		for (int i = 0; i < 12; i++) {
			password.append(chars.charAt(random.nextInt(chars.length())));
		}

		return password.toString();
	}

	public static class CreatedAccount {
		public Integer scholarId;
		public String scholarName;
		public String username;
		public String email;
		public String temporaryPassword;
		public String status;
	}

	public byte[] createScholarAccountsExcel() throws Exception {

		List<Scholars> scholars = scholarRepo.findByUserIdIsNull();

		List<CreatedAccount> results = new ArrayList<>();

		for (Scholars scholar : scholars) {
			try {
				CreatedAccount account = createUserForScholar(scholar.getScholarId());

				results.add(account);

			} catch (Exception ex) {
				CreatedAccount failed = new CreatedAccount();
				failed.scholarId = scholar.getScholarId();
				failed.scholarName = scholar.getFullName();
				failed.username = scholar.getEnrolmentno();
				failed.email = scholar.getEmail();
				failed.temporaryPassword = "";
				failed.status = "FAILED: " + ex.getMessage();

				results.add(failed);
			}
		}

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet("Scholar Credentials");

			String[] columns = { "Scholar ID", "Scholar Name", "Username", "Email", "Temporary Password", "Status" };

			Row header = sheet.createRow(0);

			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			int rowNum = 1;

			for (CreatedAccount account : results) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(account.scholarId == null ? 0 : account.scholarId);
				row.createCell(1).setCellValue(safe(account.scholarName));
				row.createCell(2).setCellValue(safe(account.username));
				row.createCell(3).setCellValue(safe(account.email));
				row.createCell(4).setCellValue(safe(account.temporaryPassword));
				row.createCell(5).setCellValue(safe(account.status));
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(output);
			return output.toByteArray();
		}
	}

	
	

@Transactional
public CreatedAccount createUserForScholar(Integer scholarId) {

    Scholars scholar = scholarRepo.findById(scholarId)
            .orElseThrow(() ->
                new RuntimeException("Scholar not found: " + scholarId));

    if (scholar.getUserId() != null) {
        throw new RuntimeException(
                "User account already exists for this scholar");
    }

    if (scholar.getEmail() == null
            || scholar.getEmail().trim().isEmpty()) {
        throw new RuntimeException("Scholar email is missing");
    }

    // Select the login identifier
    String loginIdentifier;
    UserIdentifier.IdentifierType identifierType;

    if (scholar.getEnrolmentno() != null
            && !scholar.getEnrolmentno().trim().isEmpty()) {

        loginIdentifier = scholar.getEnrolmentno().trim();
        identifierType =
                UserIdentifier.IdentifierType.ENROLLMENT_NO ;

    } else {
        // Replace getApplicationNumber() with your actual getter
        loginIdentifier = scholar.getApplicationNumber();

        if (loginIdentifier == null
                || loginIdentifier.trim().isEmpty()) {
            throw new RuntimeException(
                    "Application number is missing");
        }

        loginIdentifier = loginIdentifier.trim();
        identifierType =
                UserIdentifier.IdentifierType.APPLICATION_NO;
    }

    // Prevent duplicate login identifiers
    if (userIdentifierRepo
            .existsByIdentifierTypeAndIdentifierValue(
                    identifierType, loginIdentifier)) {
        throw new RuntimeException(
                "Login identifier already exists: " + loginIdentifier);
    }

    // Find the scholar role
    Role scholarRole = roleRepository
            .findByName(ERole.ROLE_SCHOLAR)
            .orElseThrow(() ->
                new RuntimeException("Scholar role not found"));

    // Create the user account
    UserDTO dto = new UserDTO();
    dto.setUsername("TEMP_" + java.util.UUID.randomUUID());
    dto.setPassword(java.util.UUID.randomUUID().toString());
    dto.setEmail(scholar.getEmail().trim());
    dto.setName(scholar.getFullName());
    dto.setRoleIds(
            java.util.Collections.singletonList(scholarRole.getId()));

    User user = userservice.createUser(dto);

    // Set the internal username
    user.setUsername("SCH" + user.getId());
    user = userRepo.saveAndFlush(user);

    // Store application or enrolment number as login identifier
    userservice.addUserIdentifier(
            user.getId().intValue(),
            identifierType,
            loginIdentifier);

    // Link the account to the scholar
    scholar.setUserId(user.getId());
    scholarRepo.save(scholar);
    
    
    String[] emails = user.getEmail().split("[&,;\\s]+");
    String useremail="";

    for (String email : emails) {
    	useremail = email.trim();

        if (email.isEmpty()) {
            continue;
        }
    }

    // Send the password setup link
    passwordService.sendResetLink(useremail);

    // Return account creation result
    CreatedAccount result = new CreatedAccount();
    result.scholarId = scholar.getScholarId();
    result.scholarName = scholar.getFullName();
    result.username = loginIdentifier;
    result.email = scholar.getEmail();
    result.temporaryPassword = "";
    result.status = "ACCOUNT CREATED; RESET LINK REQUESTED";

    return result;
}

	private String safe(String value) {
		return value == null ? "" : value;
	}
	
//	@Transactional
//	public User createScholarAccount(int scholarId) {
//
//	    Scholars scholar = scholarRepo.findById(scholarId)
//	        .orElseThrow(() ->
//	            new RuntimeException("Scholar not found: " + scholarId));
//
//	    // Prevent duplicate account creation
//	    if (scholar.getUserId() != null) {
//	        throw new IllegalStateException(
//	            "This scholar already has a user account");
//	    }
//
//	    // Prefer enrolment number; otherwise use application number
////	    String identifier;
////	    IdentifierType identifierType;
////
////	    if (scholar.getEnrolmentno() != null
////	            && !scholar.getEnrolmentno().trim().isEmpty()) {
////
////	        identifier = scholar.getEnrolmentno().trim();
////	        identifierType = IdentifierType.ENROLLMENT_NO;
////
////	    } else if (scholar.getApplicationNumber() != null
////	            && !scholar.getApplicationNumber().trim().isEmpty()) {
////
////	        identifier = scholar.getApplicationNumber().trim();
////	        identifierType = IdentifierType.APPLICATION_NO;
////
////	    } else {
////	        throw new IllegalStateException(
////	            "Neither enrolment number nor application number is available");
////	    }
//
//	    // Check that the login identifier is not already registered.
////	    if (userIdentifierRepo
////	            .existsByIdentifierTypeAndIdentifierValue(
////	                identifierType, identifier)) {
////	        throw new IllegalStateException(
////	            "This login identifier is already registered");
////	    }
//
//	    // Create the user using your existing user-creation logic.
//	    // Assign the SCHOLAR role and copy the scholar's email/name.
//	   // User user = createScholarUserWithoutSendingEmail(scholar);
//
//	    // Use your existing UserIdentifier entity and setters.
////	    UserIdentifier userIdentifier = new UserIdentifier();
////	    userIdentifier.setUser(user);
////	    userIdentifier.setIdentifierType(identifierType);
////	    userIdentifier.setIdentifierValue(identifier);
////
////	    userIdentifierRepo.save(userIdentifier);
//
//	    // Link the new account to the existing scholar.
//	    
//	    // Return the created user; send the reset email after commit.
//	    return user;
//	}
	@Transactional
	public User createScholarUserWithoutSendingEmail(int scholarId) {
		
		Scholars scholar = scholarRepo.findById(scholarId)
		        .orElseThrow(() ->
		            new RuntimeException("Scholar not found: " + scholarId));

		    // Prevent duplicate account creation
		    if (scholar.getUserId() != null) {
		        throw new IllegalStateException(
		            "This scholar already has a user account");
		    }

//	    if (scholar == null) {
//	        throw new IllegalArgumentException(
//	            "Scholar cannot be null");
//	    }

	    if (scholar.getEmail() == null
	            || scholar.getEmail().trim().isEmpty()) {
	        throw new IllegalArgumentException(
	            "Scholar email is required");
	    }
	    
	    // Select the login identifier
	    String loginIdentifier;
	    UserIdentifier.IdentifierType identifierType;

	    if (scholar.getEnrolmentno() != null
	            && !scholar.getEnrolmentno().trim().isEmpty()) {

	        loginIdentifier = scholar.getEnrolmentno().trim();
	        identifierType =
	                UserIdentifier.IdentifierType.ENROLLMENT_NO ;

	    } else {
	        // Replace getApplicationNumber() with your actual getter
	        loginIdentifier = scholar.getApplicationNumber();

	        if (loginIdentifier == null
	                || loginIdentifier.trim().isEmpty()) {
	            throw new RuntimeException(
	                    "Application number is missing");
	        }

	        loginIdentifier = loginIdentifier.trim();
	        identifierType =
	                UserIdentifier.IdentifierType.APPLICATION_NO;
	    }

	    // Prevent duplicate login identifiers
	    if (userIdentifierRepo
	            .existsByIdentifierTypeAndIdentifierValue(
	                    identifierType, loginIdentifier)) {
	        throw new RuntimeException(
	                "Login identifier already exists: " + loginIdentifier);
	    }

	    // Find the scholar role
	    Role scholarRole = roleRepository
	            .findByName(ERole.ROLE_SCHOLAR)
	            .orElseThrow(() ->
	                new RuntimeException("Scholar role not found"));

	    
	   

	    String email = scholar.getEmail().trim();

	    // Prevent duplicate accounts by email.
	    if (userRepo.existsByEmail(email)) {
	        throw new IllegalStateException(
	            "A user with this email already exists");
	    }

	    // Create user.
	   	    
	    //------------
	 // Create the user account
	    UserDTO dto = new UserDTO();
	    dto.setUsername("TEMP_" + java.util.UUID.randomUUID());
	    dto.setPassword(java.util.UUID.randomUUID().toString());
	    dto.setEmail(scholar.getEmail().trim());
	    dto.setName(scholar.getFullName());
	    dto.setRoleIds(
	            java.util.Collections.singletonList(scholarRole.getId()));

	    User user = userservice.createUser(dto);

	    // Set the internal username
	    user.setUsername("SCH" + user.getId());
	    //user = userRepo.saveAndFlush(user);
	    userRepo.save(user);

	    // Store application or enrolment number as login identifier
	    userservice.addUserIdentifier(
	            user.getId().intValue(),
	            identifierType,
	            loginIdentifier);
	    

	    // Link the account to the scholar
//	    scholar.setUserId(user.getId());
//	    scholarRepo.save(scholar);
//	    int updated = scholarRepo.updateUserId(
//	    		scholarId,
//	    	    user.getId()
//	    	);
	    
	  //  updateScholarUserId(scholarId,user.getId());

//	    	if (updated != 1) {
//	    	    throw new IllegalStateException(
//	    	        "Failed to update scholar user ID");
//	    	}
	    
	    return user;
//
//	    
//	    
//	    
//
//	    // Generate a random initial password.
//	    // The scholar should set a new password through the reset link.
//	    String temporaryPassword = UUID.randomUUID().toString();
//
//	    user.setPassword(passwordEncoder.encode(temporaryPassword));
//
//	    
//
//	    user.setRoles(Collections.singleton(scholarRole));
//
//	    // Save user only. Do not send email here.
//	    return userRepo.save(user);
	}
	
	

	@Transactional
	public void updateScholarUserId(int scholarId, int userId) {

	    int updated = scholarRepo.updateUserId(scholarId, userId);

	    if (updated != 1) {
	        throw new IllegalStateException(
	            "Failed to update user_id for scholar: " + scholarId);
	    }
	}
}
