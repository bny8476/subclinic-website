package com.healthcare.clinic.ambulance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "emergency_patient_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyPatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "branch_id")
    private Long branchId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private EmergencyRequest request;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "vitals_summary", columnDefinition = "TEXT")
    private String vitalsSummary;

    @Column(columnDefinition = "TEXT")
    private String interventions;

    @Column(name = "medication_administered", columnDefinition = "TEXT")
    private String medicationAdministered;

    @Column(name = "crew_notes", columnDefinition = "TEXT")
    private String crewNotes;

    @Column(name = "handover_summary", columnDefinition = "TEXT")
    private String handoverSummary;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
