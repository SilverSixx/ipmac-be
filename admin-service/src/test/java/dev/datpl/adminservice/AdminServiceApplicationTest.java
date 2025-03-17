package dev.datpl.adminservice;

import dev.datpl.adminservice.config.ConfigProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminServiceApplicationTest {

    private AdminServiceApplication adminServiceApplicationUnderTest;

    @BeforeEach
    void setUp() {
        adminServiceApplicationUnderTest = new AdminServiceApplication();
    }

    @Test
    void testMain() {
        // Setup
        // Run the test
        AdminServiceApplication.main(new String[]{"args"});

        // Verify the results
    }

    @Test
    void testConfigProperties() {
        // Setup
        // Run the test
        final ConfigProperties result = adminServiceApplicationUnderTest.configProperties();

        // Verify the results
    }
}
