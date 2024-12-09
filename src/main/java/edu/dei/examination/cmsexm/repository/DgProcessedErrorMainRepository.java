package edu.dei.examination.cmsexm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.cmsexm.model.DgExtract;
import edu.dei.examination.cmsexm.model.Dgmain;

public interface DgProcessedErrorMainRepository extends JpaRepository<Dgmain, Integer> {
	
	List <Dgmain> findByStatus(String sts);
	
	
	

}
