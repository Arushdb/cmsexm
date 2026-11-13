
package edu.dei.examination.cmsexm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.dei.examination.cmsexm.model.UserApplicationAccess;

@Repository
public interface UserApplicationAccessRepository
        extends JpaRepository<UserApplicationAccess, Integer> {

    boolean existsByUser_IdAndApplicationCode(
            Integer userId, String applicationCode);
}
