package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "permissions") // [cite: 8]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Integer permissionId; // [cite: 9]

    @Column(name = "permission_code", length = 100, unique = true, nullable = false)
    private String permissionCode; // [cite: 9]

    @Column(name = "module", length = 50, nullable = false)
    private String module; // [cite: 9]

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // [cite: 9]

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Role> roles;
}
