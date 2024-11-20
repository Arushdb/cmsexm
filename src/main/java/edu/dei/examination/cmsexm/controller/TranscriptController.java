package edu.dei.examination.cmsexm.controller;

import edu.dei.examination.cmsexm.service.TranscriptService;
import edu.dei.examination.cmsexm.model.Transcript;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/test")
public class TranscriptController {

    @Autowired
    private TranscriptService transcriptService;

    @GetMapping("/generate")
    public ResponseEntity<?> generateTranscript(@RequestParam("roll_number") String roll_number, HttpServletResponse response) {
        // Validate if roll number exists
        if (!transcriptService.rollNumberExists(roll_number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Roll Number is Incorrect.");
        }
        // Check if the student has passed the program
        if (!transcriptService.isStudentPassed(roll_number)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Roll Number Not Passed the Program.");
        }

        try {
            Transcript transcript = transcriptService.getTranscriptByRollNumber(roll_number);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=transcript_" + roll_number + ".pdf");

            transcriptService.generateTranscriptPdf(transcript, response.getOutputStream());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating transcript.");
        }
    }
}
