package com.healthcare.clinic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ClinicGrowthApplicationTests {

    @Test
    void contextLoads() {
        // Verifies that the entire Spring context (JPA, Beans, Security, SecurityFilterChain) loads cleanly
    }
}
