package edu.dei.examination.phd.service;

import java.util.List;
import java.util.Optional;


import edu.dei.examination.phd.model.ProgressReport;
import edu.dei.examination.phd.model.Publication;
import edu.dei.examination.phd.repository.ProgressReportRepository;
import edu.dei.examination.phd.repository.PublicationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;

	@Autowired
	private ProgressReportRepository progressReportRepository;

	public Publication savePublication(Integer reportId, Publication publication) {

		ProgressReport report = progressReportRepository.findById(reportId)
				.orElseThrow(() -> new RuntimeException("Progress Report not found"));

		//publication.setProgressReport(report);
		publication.setId(null);

		return publicationRepository.save(publication);
	}
	
	

	public List<Publication> getPublicationsByReportId(Integer reportId) {
		return publicationRepository.findByReportId(reportId)   ;
	}

	public Optional<Publication> updatePublication(Integer id, Integer reportId, Publication publication) {

//        Publication existing =
//                publicationRepository.findByIdAndReportId(id, reportId)
//                        .orElseThrow(() ->
//                                new RuntimeException("Publication not found"));
		return publicationRepository.findByIdAndReportId(id, reportId).map(existing -> {

			existing.setAuthors(publication.getAuthors());
			existing.setTitle(publication.getTitle());
			existing.setJournal(publication.getJournal());
			existing.setVolume(publication.getVolume());
			existing.setPageNo(publication.getPageNo());
			existing.setYear(publication.getYear());
			existing.setImpact(publication.getImpact());
			existing.setIndexing(publication.getIndexing());
			return publicationRepository.save(existing);
		});

	}

	public boolean deletePublication(Integer publicationId, Integer reportId) {

//		Publication publication = publicationRepository.findById(publicationId)
//				.orElseThrow(() -> new RuntimeException("Publication not found"));
//
//		if (!publication.getProgressReport().getId().equals(reportId)) {
//			throw new RuntimeException("Publication does not belong to this progress report");
//		}
		
		Optional<Publication> publication =
				publicationRepository.findByIdAndReportId(publicationId, reportId);

        if (!publication.isPresent()) {
            return false;
        }

		publicationRepository.delete(publication.get());
		return true;
	}

}