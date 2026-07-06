package com.powertrack.repository;

import com.powertrack.entity.User;
import com.powertrack.enums.UserRole;
import com.powertrack.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByStatus(UserStatus status);

    List<User> findByRoleAndStatus(UserRole role, UserStatus status);

    long countByRole(UserRole role);

    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND u.status = 'ACTIVE'")
    List<User> findAllActiveCustomers();

    @Query("SELECT u FROM User u WHERE u.role = 'SUPPORT' AND u.status = 'ACTIVE'")
    List<User> findAllActiveCSRs();

    @Query("SELECT u FROM User u WHERE u.role = 'FIELD_STAFF' AND u.status = 'ACTIVE'")
    List<User> findAllActiveFieldStaff();

    @Query("SELECT u FROM User u WHERE u.name LIKE %?1% OR u.email LIKE %?1%")
    List<User> searchUsers(String keyword);
}