package edu.dei.examination.cmsexm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.cmsexm.model.DgModularMain;

public interface DgModularMainRepository extends JpaRepository<DgModularMain, Integer> {
	
	List <DgModularMain> findByStatus(String sts);
	
	
	

}
