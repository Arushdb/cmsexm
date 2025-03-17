package edu.dei.examination.cmsexm.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.dei.examination.cmsexm.model.PckChange;

public interface PckChangeRepository extends JpaRepository<PckChange, Long> {
    List<PckChange> findByStatus(String status);
}
