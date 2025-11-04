package edu.dei.examination.cmsexm.service;

import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;

import edu.dei.examination.cmsexm.model.DgModularFormat;
import edu.dei.examination.cmsexm.model.DgModularMain;
import edu.dei.examination.cmsexm.model.DgModularSem;
import edu.dei.examination.cmsexm.repository.DgModularExtractRepository;
import edu.dei.examination.cmsexm.repository.DgModularMainRepository;
import edu.dei.examination.cmsexm.repository.DgModularSemRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service to read dg_modular_controller records, assemble CSVs and update status.
 *
 * Notes:
 * - Writes CSV in UTF-8 and writes BOM so Excel recognizes encoding.
 * - Uses semester_start_date from modular_sem (getSemesterStartDate()).
 * - Expects semRepository.findByProgramIdAndModuleGroup(...) to exist.
 */
@PropertySource("classpath:digilocker.properties")
@Service
public class DgModularMainServiceImpl implements DgModularMainService {

    private static final Logger logger = LoggerFactory.getLogger(DgModularMainServiceImpl.class);

    @Autowired
    private DgModularMainRepository theDgModularMainRepository;

    @Autowired
    private DgModularExtractRepository theDgModularExtractRepository;

    @Autowired
    private DgModularSemRepository semRepository;

    /**
     * Scheduled job entry. Property key referenced is left as in your code:
     * ${run-frquency.minutes}. If your properties use a different key, update accordingly.
     */
    @Transactional
    @Scheduled(fixedRateString = "${run-frquency.minutes}", timeUnit = TimeUnit.MINUTES)
    public void dgmodularmain() throws IOException {

        // STEP 1: Fetch records from dg_modular_controller where status='N'
        List<DgModularMain> controllerList = getDgProgramList("N");
        if (controllerList == null || controllerList.isEmpty()) {
            logger.info("No pending dg_modular_controller records found.");
            return;
        }

        Date runDate = new Date();
        String rundt = new SimpleDateFormat("yyyy-MM-dd").format(runDate);

        // Output directory (configurable later if desired)
        String dglocker = System.getProperty("user.home") + File.separator + "dglocker";
        File dir = new File(dglocker);
        if (!dir.exists()) {
            boolean ok = dir.mkdirs();
            if (!ok) {
                logger.warn("Could not create output directory {}", dglocker);
            }
        }

        // Get dg_format (CSV layout definition)
        List<Map<String, Object>> dgformat = theDgModularExtractRepository.getdgformat();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(dgformat);
        Type collectionType = new TypeToken<List<DgModularFormat>>() {}.getType();
        List<DgModularFormat> format = gson.fromJson(jsonElement, collectionType);

        // STEP 2: Loop through controller records
        for (DgModularMain controller : controllerList) {
            String programId = controller.getProgramId();
            String moduleGroup = controller.getModuleGroup();

            // NOTE: Use the semesterStartDate from the controller entity if that is what you intended.
            // In some prior versions you used controller.getSessionStartDate(). Here we use getSemesterStartDate() as you showed.
            Date sessionStartDate = controller.getSessionStartDate();
            String entityId = controller.getEntityId();

            logger.info("Processing DG Modular - programId={}, moduleGroup={}, sessionStartDate={}, entityId={}",
                    programId, moduleGroup, sessionStartDate, entityId);

            // STEP 3: Fetch modular_sem rows by programId and moduleGroup (no status)
            List<DgModularSem> semRows = semRepository.findByProgramIdAndModuleGroupAndSessionStartDate(programId, moduleGroup,sessionStartDate);
            if (semRows == null || semRows.isEmpty()) {
                logger.warn("No modular_sem rows found for programId={} moduleGroup={}", programId, moduleGroup,sessionStartDate);
                continue;
            }

            // Collect up to two programCourseKeys
            List<String> pckList = semRows.stream()
                    .map(DgModularSem::getProgramCourseKey)
                    .filter(k -> k != null && !k.trim().isEmpty())
                    .limit(2)
                    .collect(Collectors.toList());

            // Collect up to two semesterStartDates (this is what you want)
            List<Date> ssdList = semRows.stream()
                    .map(DgModularSem::getSemesterStartDate)
                    .filter(Objects::nonNull)
                    .limit(2)
                    .collect(Collectors.toList());

            String pck1 = pckList.size() > 0 ? pckList.get(0) : null;
            String pck2 = pckList.size() > 1 ? pckList.get(1) : pck1;
            Date ssd1 = ssdList.size() > 0 ? ssdList.get(0) : null;
            Date ssd2 = ssdList.size() > 1 ? ssdList.get(1) : ssd1;

            // STEP 4: Get total number of subjects dynamically
            List<Map<String, Object>> maxsubject = theDgModularExtractRepository.getmaxsubject(pck1, pck2, ssd1, ssd2);
            int totsub = 0;
            if (maxsubject != null && !maxsubject.isEmpty()) {
                Object val = maxsubject.get(0).get("totsub");
                String totsubc = safeToString(val);
                if (!totsubc.isEmpty()) {
                    try {
                        totsub = Integer.parseInt(totsubc);
                    } catch (NumberFormatException nfe) {
                        logger.warn("Unable to parse totsub '{}' for program {}, moduleGroup {}", totsubc, programId, moduleGroup);
                    }
                }
            }

            // STEP 5: Compute Session String (e.g., 2024-25) from semester start date (ssd1)
            String sessionStr = "NOSESSION";
            if (ssd1 != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ssd1);
                int startYear = cal.get(Calendar.YEAR);
                String endYearShort = String.valueOf((startYear + 1) % 100);
                sessionStr = startYear + "-" + endYearShort;
            }

            // STEP 6: Build CSV file name
            String fileName = entityId + "_" + programId + "_" + moduleGroup + "_" + sessionStr + "_Run_" + rundt + ".csv";
            File csvFile = new File(dglocker + File.separator + fileName);

            // Create CSV with UTF-8 BOM so Excel recognizes encoding
            try (FileOutputStream fos = new FileOutputStream(csvFile);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 CSVWriter writer = new CSVWriter(osw)) {

                // write BOM bytes first
                fos.write(0xEF);
                fos.write(0xBB);
                fos.write(0xBF);

                logger.info("Creating CSV file: {}", csvFile.getAbsolutePath());

                // CSV Header
                writecsvheader(writer, format, totsub);

                Set<String> seenRolls = new HashSet<>();

                // STEP 7: Fetch students from modular_students_sgpa
                List<Map<String, Object>> studentList = theDgModularExtractRepository
                        .getstudentlist(pck1, pck2, ssd1, ssd2, entityId);
                if (studentList == null || studentList.isEmpty()) {
                    logger.warn("No students found for programId={} moduleGroup={} (pck1={}, pck2={}, ssd1={}, ssd2={})",
                            programId, moduleGroup, pck1, pck2, ssd1, ssd2);
                    continue;
                }

                for (Map<String, Object> student : studentList) {
                    String rollno = safeToString(student.get("rroll"));
                    if (rollno.isEmpty() || !seenRolls.add(rollno)) continue;

                    // Subjects and total credit points
                    List<Map<String, Object>> subjects = theDgModularExtractRepository
                            .getsubjectlist(rollno, pck1, pck2);
                    Map<String, Object> creditpoint = theDgModularExtractRepository
                            .gettotcreditpoint(rollno, pck1, pck2);

                    String sem = safeToString(student.get("sem")); // case-insensitive depending on DB alias
                    String roman = getroman(sem);

                    writecsv(student, format, writer, subjects, creditpoint,
                            subjects != null ? subjects.size() : 0, roman);
                }

                // flush to ensure file content is written
                writer.flush();
                logger.info("✅ CSV generated successfully: {}", csvFile.getAbsolutePath());

                // STEP 8: Update status in dg_modular_controller
              //  theDgModularExtractRepository.updateDgModularControlStatus(runDate, programId, moduleGroup, sessionStartDate);
              //  logger.info("Status updated for controller: programId={} moduleGroup={} sessionStartDate={}", programId, moduleGroup, sessionStartDate);

             // STEP 8: Update status in both controller and modular_sem
                try {
                    // 1️⃣ Update dg_modular_controller
                    theDgModularExtractRepository.updateDgModularControlStatus(runDate, programId, moduleGroup, sessionStartDate);

                    // 2️⃣ Update modular_sem
                    semRepository.updateModularSemStatusToCompleted(programId, moduleGroup, sessionStartDate);

                    logger.info("Status updated to 'C' for both dg_modular_controller and modular_sem: programId={} moduleGroup={} sessionStartDate={}",
                            programId, moduleGroup, sessionStartDate);
                } catch (Exception e) {
                    logger.error("Error updating status for programId={}, moduleGroup={}, sessionStartDate={}: {}",
                            programId, moduleGroup, sessionStartDate, e.getMessage(), e);
                }

                
            } catch (Exception ex) {
                logger.error("Error while generating CSV for programId={}, moduleGroup={}, sessionStartDate={}: {}",
                        programId, moduleGroup, sessionStartDate, ex.getMessage(), ex);
            }
        }
    }

    // ---------------- Interface method implementations ----------------

    @Override
    public List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndSessionStartDate(
            String programId, String moduleGroup, Date sessionStartDate) {
        // If you need to filter by sessionStartDate here, change repository signature accordingly.
        return semRepository.findByProgramIdAndModuleGroupAndSessionStartDate(programId, moduleGroup,sessionStartDate);
    }

    @Override
    public List<DgModularMain> getDgProgramList(String status) {
        return theDgModularMainRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public int updateDgModularControlStatus(Date runtime, String programId, String modularGroup, Date sessionStartDate) {
        return theDgModularExtractRepository.updateDgModularControlStatus(runtime, programId, modularGroup, sessionStartDate);
    }

    // ---------------- Helper methods ----------------

    private String safeToString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    private String getroman(String sem) {
        if (sem == null) return "";
        String[] sm = {"S1", "S2", "S3", "S4", "S5", "S6", "S7", "S8",
                "S9", "S10", "S11", "S12", "S13", "S14", "S15", "S16"};
        String[] rom = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII",
                "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI"};
        for (int i = 0; i < sm.length; i++) {
            if (sem.equalsIgnoreCase(sm[i])) return rom[i];
        }
        return "";
    }

    private void writecsvheader(CSVWriter writer, List<DgModularFormat> format, int totsub) {
        String[] tempheader = new String[15];
        String[] header = {"SUB*NM", "SUB*", "SUB*_TH_MAX", "SUB*_PR_MAX", "SUB*_CE_MAX", "SUB*_TH_MRKS",
                "SUB*_PR_MRKS", "SUB*_CE_MRKS", "SUB*_TOT", "SUB*_GRADE", "SUB*_GRADE_POINTS",
                "SUB*_CREDIT", "SUB*_CREDIT_POINTS", "SUB*_REMARKS", "SUB*_CREDIT_ELIGIBILITY"};

        String keyfield;
        String[] arr = new String[Math.max(1, format.size() + totsub * 15 - 1)];
        int n = -1;
        for (DgModularFormat fmt : format) {
            keyfield = fmt.getField();
            if (keyfield == null) continue;
            if (keyfield.equalsIgnoreCase("subject")) {
                for (int j = 1; j <= totsub; j++) {
                    convertheader(tempheader, header, j);
                    for (String s : tempheader) arr[++n] = s;
                }
            } else {
                arr[++n] = keyfield;
            }
        }
        writer.writeNext(arr);
    }

    private void convertheader(String[] tempheader, String[] header, int j) {
        for (int i = 0; i < header.length; i++) tempheader[i] = header[i].replace("*", String.valueOf(j));
    }

    private void writecsv(Map<String, Object> student, List<DgModularFormat> format,
                          CSVWriter writer, List<Map<String, Object>> subjects,
                          Map<String, Object> creditpoint, int totsub, String roman) {

        String[] arr = new String[format.size() + (totsub * 15)];
        int n = -1;
        for (DgModularFormat fmt : format) {
            String keyfield = fmt.getField();
            if (keyfield == null) continue;
            if (keyfield.equalsIgnoreCase("SEM")) {
                arr[++n] = roman;
                continue;
            }

            if (keyfield.equalsIgnoreCase("subject")) {
                if (subjects != null) {
                    for (Map<String, Object> sub : subjects) {
                        arr[++n] = safeToString(sub.get("course_name"));
                        arr[++n] = safeToString(sub.get("course_code"));
                        for (int j = 0; j < 8; j++) arr[++n] = "";
                        arr[++n] = safeToString(sub.get("gradepoint"));
                        arr[++n] = safeToString(sub.get("credits"));
                        arr[++n] = safeToString(sub.get("creditpoint"));
                        for (int j = 0; j < 2; j++) arr[++n] = "";
                    }
                }
                continue;
            }

            if (keyfield.equalsIgnoreCase("TOT_CREDIT_POINTS")) {
                arr[++n] = safeToString(creditpoint != null ? creditpoint.get("TOT_CREDIT_POINTS") : null);
            } else if (keyfield.equalsIgnoreCase("TOT_CREDIT")) {
                arr[++n] = safeToString(creditpoint != null ? creditpoint.get("TOT_CREDIT") : null);
            } else {
                arr[++n] = safeToString(student.get(keyfield));
            }
        }
        writer.writeNext(arr);
    }
}
