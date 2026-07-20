package com.busbooking.system.repository;

import com.busbooking.system.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    boolean existsByPermissionCode(String permissionCode);
    Optional<Permission> findByPermissionCode(String permissionCode);
}
