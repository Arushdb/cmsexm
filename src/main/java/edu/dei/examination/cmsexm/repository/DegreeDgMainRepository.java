package edu.dei.examination.cmsexm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.dei.examination.cmsexm.model.DegreeDgExtract;
import edu.dei.examination.cmsexm.model.DegreeDgMain;

public interface DegreeDgMainRepository extends JpaRepository<DegreeDgMain, Integer> {
	
	List <DegreeDgMain> findByStatus(String sts);
	
	
	

}
