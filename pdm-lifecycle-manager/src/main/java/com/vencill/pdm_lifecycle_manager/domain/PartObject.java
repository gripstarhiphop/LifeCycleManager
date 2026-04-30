package com.vencill.pdm_lifecycle_manager.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class PartObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String partNumber;

    @Enumerated(EnumType.STRING)
    private partType type;

    @Enumerated(EnumType.STRING)
    private lifecyclestate state;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PartObject parent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (state == null) state = lifecyclestate.DRAFT;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
