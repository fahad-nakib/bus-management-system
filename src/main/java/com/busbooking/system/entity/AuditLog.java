package com.busbooking.system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "audit_logs") // [cite: 17]
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_log_id")
    private Long auditLogId; // [cite: 18]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // [cite: 19]

    @Column(name = "action_type", length = 50, nullable = false)
    private String actionType; // [cite: 19]

    @Column(name = "entity_type", length = 50, nullable = false)
    private String entityType; // [cite: 19]

    @Column(name = "entity_id", nullable = false)
    private Long entityId; // [cite: 19]

    @Column(name = "old_value", columnDefinition = "JSONB")
    private String oldValue; // [cite: 19]

    @Column(name = "new_value", columnDefinition = "JSONB")
    private String newValue; // [cite: 19]

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // [cite: 19]

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent; // [cite: 19]

    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now(); // [cite: 19]
}
