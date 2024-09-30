package edu.dei.examination.cmsexm.controller;



import edu.dei.examination.cmsexm.service.TranscriptService;
import edu.dei.examination.cmsexm.model.Transcript;



import org.springframework.beans.factory.annotation.Autowired;
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
    public void generateTranscript(@RequestParam("roll_number") String roll_number, HttpServletResponse response) {
        try {
            Transcript transcript = transcriptService.getTranscriptByRollNumber(roll_number);
            String imagePath = "/images/deiLogoHeader.png";

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=transcript_" + roll_number + ".pdf");

            transcriptService.generateTranscriptPdf(transcript, response.getOutputStream(), imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

