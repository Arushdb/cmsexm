package edu.dei.examination.cmsexm.service;

import edu.dei.examination.cmsexm.model.Transcript;

public interface TranscriptService {
    Transcript findTranscriptByRollNumber(String roll_number);
}
