package edu.dei.examination.cmsexm.service;

import edu.dei.examination.cmsexm.model.Transcript;

public interface TranscriptDao {
    Transcript findTranscriptByRollNumber(String roll_number);
}
