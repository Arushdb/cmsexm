package edu.dei.examination.cmsexm.service;

import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import edu.dei.examination.cmsexm.model.DegreeDgExtract;
import edu.dei.examination.cmsexm.model.DegreeDgFormat;
import edu.dei.examination.cmsexm.model.DegreeDgMain;
import edu.dei.examination.cmsexm.repository.DegreeDgExtractRepository;
import edu.dei.examination.cmsexm.repository.DegreeDgMainRepository;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@PropertySource("classpath:digilocker.properties")
@Service
public class DegreeDgMainServiceImpl implements DegreeDgMainService {

    @Autowired
    private DegreeDgMainRepository theDegreeDgMainRepository;

    @Autowired
    private DegreeDgExtractRepository theDegreeDgExtractRepository;

    /**
     * This scheduled method processes unprocessed data every fixed interval.
     * It retrieves a list of DegreeDgMain records (programs) with status "N".
     * For each program record, it fetches the student data (using getdegreestudentlist)
     * and writes a CSV file in the order defined by getdegreedgformat.
     * Finally, it updates the status to 'C' for that program.
     */
    @Transactional
    @Scheduled(fixedRateString = "${run-frquency.minutes}", timeUnit = TimeUnit.MINUTES)
    public void degreedgmain() throws IOException {

        // Get list of programs with status "N"
        List<DegreeDgMain> programList = getDegreeDgprogramlist("N");
        Date rundate = new Date();
        String runDateStr = new SimpleDateFormat("yyyy-MM-dd").format(rundate);

        // Determine output directory (e.g., user's home/dgreedglocker)
        String currentUsersHomeDir = System.getProperty("user.home");
        String dgLocker = currentUsersHomeDir + File.separator + "degreedglocker";
        File theDir = new File(dgLocker);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        // Create a log CSV file to record processed program details
        FileWriter logFileWriter = new FileWriter(new File(dgLocker + File.separator + "log_Run_" + runDateStr + ".csv"));
        CSVWriter logWriter = new CSVWriter(logFileWriter);

        // Get the header order from degree_dg_format
        List<Map<String, Object>> formatList = theDegreeDgExtractRepository.getdegreedgformat();
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(formatList);
        Type collectionType = new TypeToken<List<DegreeDgFormat>>(){}.getType();
        List<DegreeDgFormat> format = gson.fromJson(jsonElement, collectionType);

        // Process each program record
        for (DegreeDgMain programObj : programList) {
            // Fetch student data for the given program id and session start date
            List<Map<String, Object>> studentData = theDegreeDgExtractRepository.getdegreestudentlist(
                    programObj.getProgramId(), programObj.getSessionStartDate());
            if (studentData == null || studentData.isEmpty()) {
                continue; // Skip if no student data found
            }

            // Generate a CSV file name based on the course name and program ID
            // (Assuming the query returns a column named COURSE_NAME in uppercase)
            String courseName = (String) studentData.get(0).get("COURSE_NAME");
            String fileCourseName = (courseName != null) ? courseName.replaceAll(" ", "_") : "UnknownCourse";
            String fileName = fileCourseName + "_" + programObj.getProgramId()+ "_"+programObj.getSessionStartDate() + "_Run_" + runDateStr + ".csv";
            FileWriter outputFile = new FileWriter(new File(dgLocker + File.separator + fileName));
            CSVWriter writer = new CSVWriter(outputFile);

            // Write CSV header in the order defined by getdegreedgformat
            String[] header = new String[format.size()];
            for (int i = 0; i < format.size(); i++) {
                header[i] = format.get(i).getField();  // Assumes getField() returns the header name
            }
            writer.writeNext(header);

            // Write each student record in the order specified by header
            for (Map<String, Object> student : studentData) {
                String[] row = new String[format.size()];
                for (int i = 0; i < format.size(); i++) {
                    String key = format.get(i).getField();
                    Object value = student.get(key);
                    row[i] = (value != null) ? value.toString() : "";
                }
                writer.writeNext(row);
            }

            // Write a log entry for this program
            String[] logEntry = { programObj.getProgramId() + "_" +
                                  programObj.getSessionStartDate() + "_" +
                                  programObj.getSessionEndDate() };
            logWriter.writeNext(logEntry);

            // Update the status for this program to 'C'
            theDegreeDgExtractRepository.updatestatus(rundate, programObj.getProgramId(), programObj.getSessionStartDate());

            writer.close();
        }
        logWriter.close();
    }

    @Override
    public List<DegreeDgMain> getDegreeDgprogramlist(String status) {
        return theDegreeDgMainRepository.findByStatus(status);
    }
}
