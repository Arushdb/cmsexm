package edu.dei.examination.phd.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import edu.dei.examination.phd.model.Attendence;
import edu.dei.examination.phd.repository.AttendenceRepository;

@Service
public class AttendenceServiceImpl  implements AttendenceService{

	@Autowired
	AttendenceRepository attendenceRepository; 
	
	@Autowired
	@Qualifier("phdEntityManagerFactory")
	EntityManager em;
	@Override
	public List<Attendence> findAll() {
		// TODO Auto-generated method stub
		List<Attendence>  atn= attendenceRepository.findAll();
		return atn;
	}

}
