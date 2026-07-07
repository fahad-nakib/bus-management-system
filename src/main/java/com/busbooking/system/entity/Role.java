package com.busbooking.system.entity;
import com.busbooking.system.entity.enums.RoleNameEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "roles") // [cite: 6]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId; // [cite: 7]

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private RoleNameEnum roleName; // [cite: 7, 64]

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // [cite: 7]

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt; // [cite: 7]

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permissions", // [cite: 12]
            joinColumns = @JoinColumn(name = "role_id"), // [cite: 13]
            inverseJoinColumns = @JoinColumn(name = "permission_id") // [cite: 13]
    )
    private Set<Permission> permissions;
}
