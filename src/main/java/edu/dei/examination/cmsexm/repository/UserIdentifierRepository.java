package edu.dei.examination.cmsexm.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.dei.examination.cmsexm.model.UserIdentifier;



public interface UserIdentifierRepository
        extends JpaRepository<UserIdentifier, Integer> {

    /* =========================================
       Find identifier by its value (login input)
       ========================================= */
    Optional<UserIdentifier> findByIdentifierValue(String identifierValue);

    /* =========================================
       Same lookup but ACTIVE only (recommended)
       ========================================= */
    Optional<UserIdentifier> findByIdentifierValueAndStatus(
            String identifierValue, String status);
    
    
    List<UserIdentifier> findByUserId(Integer userId);

   

    /* =========================================
       (Optional) Linear-style join with User
       ========================================= */
    @Query(
        "SELECT ui " +
        "FROM UserIdentifier ui " +
        "WHERE ui.identifierValue = :loginId " +
        "AND ui.status = 'ACTIVE'"
    )
    Optional<UserIdentifier> findActiveByLoginId(
            @Param("loginId") String loginId);
}
