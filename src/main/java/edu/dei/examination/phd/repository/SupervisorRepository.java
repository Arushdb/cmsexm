package edu.dei.examination.phd.repository;


import edu.dei.examination.phd.model.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupervisorRepository extends JpaRepository<Supervisor, Integer> {

    // Get supervisor using logged-in user
    Optional<Supervisor> findByUserId(Integer userId);

}