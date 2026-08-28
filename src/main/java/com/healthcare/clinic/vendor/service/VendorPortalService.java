package com.healthcare.clinic.vendor.service;

import com.healthcare.clinic.vendor.entity.VendorDelivery;
import com.healthcare.clinic.vendor.repository.VendorDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorPortalService {

    private final VendorDeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public List<VendorDelivery> getVendorDeliveries(Long vendorUserId) {
        return deliveryRepository.findByVendorUserId(vendorUserId);
    }

    @Transactional
    public VendorDelivery createDelivery(Long poId, VendorDelivery deliveryInput, Long vendorUserId) {
        deliveryInput.setPoId(poId);
        deliveryInput.setVendorUserId(vendorUserId);
        deliveryInput.setStatus("DISPATCHED");
        return deliveryRepository.save(deliveryInput);
    }
}
