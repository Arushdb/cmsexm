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
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("classpath:digilocker.properties")
@Service
public class DgModularMainServiceImpl implements DgModularMainService {

    @Autowired
    private DgModularMainRepository theDgModularMainRepository;

    @Autowired
    private DgModularExtractRepository theDgModularExtractRepository;

    @Autowired
    private DgModularSemRepository semRepository;

    @Transactional
    @Scheduled(fixedRateString = "${run-frquency.minutes}", timeUnit = TimeUnit.MINUTES)
    public void dgmodularmain() throws IOException {

        List<DgModularMain> programList = getDgProgramList("N");
        Date runDate = new Date();
        String rundt = new SimpleDateFormat("yyyy-MM-dd").format(runDate);

        String dglocker = System.getProperty("user.home") + File.separator + "dglocker";
        File dir = new File(dglocker);
        if (!dir.exists()) dir.mkdirs();

        List<Map<String, Object>> dgformat = theDgModularExtractRepository.getdgformat();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(dgformat);
        Type collectionType = new TypeToken<List<DgModularFormat>>() {}.getType();
        List<DgModularFormat> format = gson.fromJson(jsonElement, collectionType);

        for (DgModularMain programObj : programList) {

            String programId = programObj.getProgramId();
            String moduleGroup = programObj.getModuleGroup();
            String entityId = programObj.getEntityId();

            List<DgModularSem> semRows = semRepository.findByProgramIdAndModuleGroupAndStatus(
                    programId, moduleGroup, "N");
            if (semRows == null || semRows.isEmpty()) continue;

            List<String> pckList = semRows.stream()
                    .map(DgModularSem::getProgramCourseKey)
                    .filter(k -> k != null && !k.trim().isEmpty())
                    .collect(Collectors.toList());

            List<Date> ssdList = semRows.stream()
                    .map(DgModularSem::getSessionStartDate)
                    .collect(Collectors.toList());

            String pck1 = pckList.size() > 0 ? pckList.get(0) : null;
            String pck2 = pckList.size() > 1 ? pckList.get(1) : pck1;

            Date ssd1 = ssdList.size() > 0 ? ssdList.get(0) : null;
            Date ssd2 = ssdList.size() > 1 ? ssdList.get(1) : ssd1;

            // Get max subjects dynamically
            List<Map<String, Object>> maxsubject = theDgModularExtractRepository.getmaxsubject(pck1, pck2, ssd1, ssd2);
            int totsub = 0;
            if (maxsubject != null && !maxsubject.isEmpty()) {
                String totsubc = (String) maxsubject.get(0).get("totsub");
                totsub = Integer.parseInt(totsubc);
            }
            
            String sessionStr = "NOSESSION";
            if (ssd1 != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(ssd1);

                int startYear = cal.get(Calendar.YEAR);
                String endYearShort = String.valueOf((startYear + 1) % 100); // last two digits

                sessionStr = startYear + "-" + endYearShort;
            }

            

            // Create dynamic CSV file for each program and module group
           // String fileName = entityId +"_" + programId + "_" + moduleGroup + "_Run_" + rundt + ".csv";
            String fileName = entityId + "_" + programId + "_" + moduleGroup + "_" + sessionStr + "_Run_" + rundt + ".csv";
            File csvFile = new File(dglocker + File.separator + fileName);

            try (FileWriter outputfile = new FileWriter(csvFile);
                 CSVWriter writer = new CSVWriter(outputfile)) {

                writecsvheader(writer, format, totsub);

                Set<String> seenRolls = new HashSet<>();

                // Fetch students for PCKs
                List<Map<String, Object>> studentList = theDgModularExtractRepository
                        .getstudentlist(pck1, pck2, ssd1, ssd2,entityId);
                if (studentList == null) continue;

                for (Map<String, Object> student : studentList) {
                    String rollno = safeToString(student.get("rroll"));
                    if (rollno.isEmpty() || !seenRolls.add(rollno)) continue;

                    List<Map<String, Object>> subjects = theDgModularExtractRepository
                            .getsubjectlist(rollno, pck1, pck2);

                    Map<String, Object> creditpoint = theDgModularExtractRepository
                            .gettotcreditpoint(rollno, pck1, pck2);

                    String sem = safeToString(student.get("SEM"));
                    String roman = getroman(sem);

                    writecsv(student, format, writer, subjects, creditpoint,
                            subjects != null ? subjects.size() : 0, roman);
                }

                // Update status for both semester start dates
                theDgModularExtractRepository.updatestatus(runDate, programId, moduleGroup, ssd1);
                theDgModularExtractRepository.updatestatus(runDate, programId, moduleGroup, ssd2);
            }
        }
    }

    private String getroman(String sem) {
        String[] sm = {"S1","S2","S3","S4","S5","S6","S7","S8","S9","S10","S11","S12","S13","S14","S15","S16"};
        String[] rom = {"I","II","III","IV","V","VI","VII","VIII","IX","X","XI","XII","XIII","XIV","XV","XVI"};
        for (int i = 0; i < sm.length; i++) if (sem.equalsIgnoreCase(sm[i])) return rom[i];
        return "";
    }

    private void writecsvheader(CSVWriter writer, List<DgModularFormat> format, int totsub) {
        String[] tempheader = new String[15];
        String[] header = {"SUB*NM","SUB*","SUB*_TH_MAX","SUB*_PR_MAX","SUB*_CE_MAX","SUB*_TH_MRKS","SUB*_PR_MRKS","SUB*_CE_MRKS",
                "SUB*_TOT","SUB*_GRADE","SUB*_GRADE_POINTS","SUB*_CREDIT","SUB*_CREDIT_POINTS","SUB*_REMARKS","SUB*_CREDIT_ELIGIBILITY"};

        String keyfield;
        String[] arr = new String[format.size() + totsub * 15 - 1];
        int n = -1;
        Iterator<DgModularFormat> fmt = format.iterator();
        for (int i = 0; i < format.size(); i++) {
            keyfield = fmt.next().getField();
            if (keyfield.equalsIgnoreCase("subject")) {
                for (int j = 1; j <= totsub; j++) {
                    convertheader(tempheader, header, j);
                    for (String s : tempheader) arr[++n] = s;
                }
            } else arr[++n] = keyfield;
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
        Iterator<DgModularFormat> fmt = format.iterator();
        int subcount = totsub - (subjects != null ? subjects.size() : 0);
        subcount = Math.max(0, subcount * 15);
        int n = -1;

        for (int i = 0; i < format.size(); i++) {
            String keyfield = fmt.next().getField();
            if (keyfield.equalsIgnoreCase("SEM")) { arr[++n] = roman; continue; }

            if (keyfield.equalsIgnoreCase("subject")) {
                if (subjects != null) {
                    for (Map<String, Object> stdsubjects : subjects) {
                        arr[++n] = safeToString(stdsubjects.get("course_name"));
                        arr[++n] = safeToString(stdsubjects.get("course_code"));
                        for (int j = 0; j < 8; j++) arr[++n] = "";
                        arr[++n] = safeToString(stdsubjects.get("gradepoint"));
                        arr[++n] = safeToString(stdsubjects.get("credits"));
                        arr[++n] = safeToString(stdsubjects.get("creditpoint"));
                        for (int j = 0; j < 2; j++) arr[++n] = "";
                    }
                }
                continue;
            }

            if (keyfield.equalsIgnoreCase("TOT_CREDIT_POINTS")) arr[++n] = safeToString(creditpoint.get("TOT_CREDIT_POINTS"));
            else if (keyfield.equalsIgnoreCase("TOT_CREDIT")) arr[++n] = safeToString(creditpoint.get("TOT_CREDIT"));
            else {
                if (keyfield.equalsIgnoreCase("AADHAAR_NAME")) for (int q = 0; q < subcount; q++) arr[++n] = "";
                arr[++n] = safeToString(student.get(keyfield));
            }
        }
        writer.writeNext(arr);
    }

    @Override
    public List<DgModularMain> getDgProgramList(String status) {
        return theDgModularMainRepository.findByStatus(status);
    }

    private String safeToString(Object o) {
        return o == null ? "" : String.valueOf(o);
    }

    @Override
    public List<DgModularSem> getSemestersByProgramIdAndModuleGroupAndStatus(String programId, String moduleGroup, String status) {
        return semRepository.findByProgramIdAndModuleGroupAndStatus(programId, moduleGroup, status);
    }
}
