package com.healthcare.clinic.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClinicContextRetriever {

    public String buildSanitizedContext(Long userId, String userRole) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[REAL-TIME CLINIC INFORMATION]\n");
        sb.append("- Clinic Name: Aurelian Healthcare Center\n");
        sb.append("- Operating Hours: Monday to Saturday, 08:00 AM - 08:00 PM. Emergency Services: 24/7.\n");
        sb.append("- Contact Support: +1 (800) 555-CLINIC / support@aurelianhealth.com\n");
        sb.append("- Location: 100 Healthcare Boulevard, Suite 400, Medical City\n");

        if (userId != null) {
            sb.append("\n[AUTHENTICATED USER CONTEXT]\n");
            sb.append("- User ID: ").append(userId).append("\n");
            sb.append("- Role: ").append(userRole != null ? userRole : "USER").append("\n");
        }

        return sb.toString();
    }
}
