package com.busbooking.system.repository;

import com.busbooking.system.entity.Role;
import com.busbooking.system.entity.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    boolean existsByRoleName(RoleNameEnum roleName);
    Optional<Role> findByRoleName(RoleNameEnum roleName);
}
