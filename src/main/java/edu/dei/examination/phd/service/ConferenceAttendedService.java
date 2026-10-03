package edu.dei.examination.phd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.dei.examination.phd.model.ConferenceAttended;
import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.Publication;
import edu.dei.examination.phd.repository.ConferenceAttendedRepository;
import edu.dei.examination.phd.repository.ProgressReportRepository;

@Service
public class ConferenceAttendedService {

    private final ConferenceAttendedRepository repository;
    @Autowired
	private ProgressReportRepository progressReportRepository;

    public ConferenceAttendedService(
            ConferenceAttendedRepository repository) {
        this.repository = repository;
    }

    // ============================
    // Get all conferences
    // belonging to a report
    // ============================

    public List<ConferenceAttended> getByReportId(Integer reportId) {
        return repository.findByReportId(reportId);
    }


    // ============================
    // Get one conference
    // only if it belongs to report
    // ============================

    public Optional<ConferenceAttended> getByIdAndReportId(
            Integer id,
            Integer reportId) {

        return repository.findByIdAndReportId(id, reportId);
    }


    // ============================
    // Save / Update
    // ============================
//
//    public ConferenceAttended save(
//            ConferenceAttended conference) {
//
//        return repository.save(conference);
//    }
    
    public ConferenceAttended save(Integer reportId, ConferenceAttended conference) {

		ProgressReport report = progressReportRepository.findById(reportId)
				.orElseThrow(() -> new RuntimeException("Progress Report not found"));

		//publication.setProgressReport(report);
		conference.setId(null);

		return repository.save(conference);
	}


    // ============================
    // Update existing conference
    // only if it belongs to report
    // ============================

    public Optional<ConferenceAttended> update(
            Integer id,
            Integer reportId,
            ConferenceAttended updated) {

        Optional<ConferenceAttended> existing =
                repository.findByIdAndReportId(id, reportId);

        if (existing.isEmpty()) {
            return Optional.empty();
        }

        ConferenceAttended conference = existing.get();

        conference.setAuthors(updated.getAuthors());
        conference.setTitle(updated.getTitle());
        conference.setType(updated.getType());
        conference.setLevel(updated.getLevel());
        conference.setOrganizer(updated.getOrganizer());
        conference.setPlace(updated.getPlace());
        conference.setDates(updated.getDates());
        conference.setPresentationType(
                updated.getPresentationType());
        conference.setParticipation(
                updated.getParticipation());
        conference.setFunding(updated.getFunding());

        return Optional.of(repository.save(conference));
    }


    // ============================
    // Delete
    // only if conference belongs
    // to supplied report
    // ============================

    public boolean delete(
            Integer id,
            Integer reportId) {

        Optional<ConferenceAttended> conference =
                repository.findByIdAndReportId(id, reportId);

        if (conference.isEmpty()) {
            return false;
        }

        repository.delete(conference.get());

        return true;
    }
}