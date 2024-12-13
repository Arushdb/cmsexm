package edu.dei.examination.cms.service;

import edu.dei.examination.cms.model.Transcript;

public interface TranscriptService {
    Transcript findTranscriptByRollNumber(String roll_number);
}
