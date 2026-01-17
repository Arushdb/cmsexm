package edu.dei.examination.phd.repository;

import edu.dei.examination.phd.model.RegSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import javax.persistence.LockModeType;

import java.util.Optional;

public interface RegSequenceRepository extends JpaRepository<RegSequence, Integer> {

    Optional<RegSequence> findByUnivCodeAndProgramCodeAndYear(String univCode, String programCode, Integer year);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RegSequence r WHERE r.univCode = :u AND r.programCode = :p AND r.year = :y")
    Optional<RegSequence> findForUpdate(@Param("u") String univCode,
                                        @Param("p") String programCode,
                                        @Param("y") Integer year);
}
