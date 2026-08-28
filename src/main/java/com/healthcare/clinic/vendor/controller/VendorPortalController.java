package com.healthcare.clinic.vendor.controller;

import com.healthcare.clinic.security.UserPrincipal;
import com.healthcare.clinic.vendor.entity.VendorDelivery;
import com.healthcare.clinic.vendor.service.VendorPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR') or hasRole('SUPER_ADMIN') or hasRole('INVENTORY_MANAGER')")
public class VendorPortalController {

    private final VendorPortalService vendorService;

    @GetMapping("/deliveries")
    public ResponseEntity<List<VendorDelivery>> getDeliveries(@AuthenticationPrincipal UserPrincipal user) {
        Long vendorUserId = user != null ? user.getUserId() : null;
        return ResponseEntity.ok(vendorService.getVendorDeliveries(vendorUserId));
    }

    @PostMapping("/purchase-orders/{poId}/dispatch")
    public ResponseEntity<VendorDelivery> createDelivery(
            @PathVariable Long poId,
            @RequestBody VendorDelivery delivery,
            @AuthenticationPrincipal UserPrincipal vendorUser) {
        Long vendorUserId = vendorUser != null ? vendorUser.getUserId() : null;
        return ResponseEntity.ok(vendorService.createDelivery(poId, delivery, vendorUserId));
    }
}
