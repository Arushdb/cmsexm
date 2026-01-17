package edu.dei.examination.phd.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.dei.examination.phd.dto.ActiveSemesterDTO;
import edu.dei.examination.phd.model.Semesters;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SemesterRepository extends JpaRepository<Semesters, Integer> {

  
    // ✅ Find active semesters (if you use an "isActive" column)
    //List<Semesters> findByIsActiveTrueOrderByStartDateAsc(); 
    

    
    @Query("SELECT s FROM Semesters s WHERE s.active = true "+
           "AND :today BETWEEN s.regStartDate AND s.regEndDate"
    		     		     
    		)
    		List<Semesters> findActiveSemester(@Param("today") LocalDate today);
        ;

    
    
}
