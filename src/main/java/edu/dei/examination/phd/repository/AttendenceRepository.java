package edu.dei.examination.phd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.dei.examination.phd.model.Attendence;

public interface AttendenceRepository extends JpaRepository<Attendence,Integer> {

}
