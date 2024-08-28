package edu.dei.examination.phd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.dei.examination.phd.model.Attendence;
import edu.dei.examination.phd.service.AttendenceService;

@RestController
@RequestMapping("/api/phd")
public class PhdController  {
	
	@Autowired
	private AttendenceService attendenceService ;

	
	
	
	@GetMapping("/attendence")
	public List<Attendence> findAll() {
		List<Attendence> att =attendenceService.findAll();
		return att;
		
	}

}
