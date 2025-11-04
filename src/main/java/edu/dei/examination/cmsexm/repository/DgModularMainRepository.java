package edu.dei.examination.cmsexm.repository;

import edu.dei.examination.cmsexm.model.DgModularMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface DgModularMainRepository extends JpaRepository<DgModularMain, Integer> {

    // ✅ Existing usage (optional)
    List<DgModularMain> findByStatus(String status);

    // ✅ Fetch dg_modular_controller pending records (status = 'N')
    @Query(
        value = "SELECT * " +
                "FROM exam_live.dg_modular_controller " +
                "WHERE program_id = ?1 " +
                "AND module_group = ?2 " +
                "AND session_start_date = ?3 " +
                "AND status = 'N'",
        nativeQuery = true
    )
    List<Object[]> findPendingControllers(String programId, String moduleGroup, Date sessionStartDate);

    // ✅ Update dg_modular_controller.status = 'C' after CSV generation
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
        value = "UPDATE exam_live.dg_modular_controller " +
                "SET status = 'C', run_time = ?1 " +
                "WHERE program_id = ?2 " +
                "AND module_group = ?3 " +
                "AND session_start_date = ?4",
        nativeQuery = true
    )
    int updateControllerStatusToCompleted(Date runTime, String programId, String moduleGroup, Date sessionStartDate);
}
