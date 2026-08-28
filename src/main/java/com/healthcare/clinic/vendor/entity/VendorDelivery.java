package com.healthcare.clinic.vendor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vendor_deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "po_id", nullable = false)
    private Long poId;

    @Column(name = "vendor_user_id", nullable = false)
    private Long vendorUserId;

    @Column(name = "tracking_number", nullable = false, length = 100)
    private String trackingNumber;

    @Column(length = 100)
    private String carrier;

    @Column(name = "dispatch_date", nullable = false)
    @Builder.Default
    private LocalDate dispatchDate = LocalDate.now();

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DISPATCHED"; // DISPATCHED, IN_TRANSIT, DELIVERED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;
}
