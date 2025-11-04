package edu.dei.examination.cmsexm.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import edu.dei.examination.cmsexm.model.DgModularSem;

public interface DgModularSemRepository extends JpaRepository<DgModularSem, Integer> {

    // ✅ Custom finder method using @Query (since Spring Data can’t infer Date column names automatically)
	@Query(value = "SELECT * FROM exam_live.modular_sem " +
            "WHERE program_id = ?1 " +
            "AND module_group = ?2 " +
            "AND session_start_date = ?3",
    nativeQuery = true)

    List<DgModularSem> findByProgramIdAndModuleGroupAndSessionStartDate(
            String programId,
            String moduleGroup,
            Date sessionStartDate
    );

    // ✅ Update modular_sem status to 'C' after CSV generation
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE exam_live.modular_sem " +
            "SET status = 'C' " +
            "WHERE program_id = ?1 " +
            "AND module_group = ?2 " +
            "AND session_start_date = ?3",
    nativeQuery = true)

    int updateModularSemStatusToCompleted(
            String programId,
            String moduleGroup,
            Date sessionStartDate
    );
}
