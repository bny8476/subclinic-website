package com.healthcare.clinic.engagement.listener;

import com.healthcare.clinic.engagement.entity.SurveyTemplate;
import com.healthcare.clinic.engagement.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SurveyTriggerListener {

    private final SurveyService surveyService;

    public void triggerPostAppointmentSurveys(Long appointmentId) {
        log.info("Triggering post-appointment surveys for Appointment ID {}", appointmentId);

        List<SurveyTemplate> templates = surveyService.getTemplatesByContext(SurveyTemplate.TriggerContext.POST_APPOINTMENT);
        
        if (!templates.isEmpty()) {
            log.info("Found {} survey templates for post-appointment.", templates.size());
        }
    }
}
