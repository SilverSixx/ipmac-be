package dev.datpl.trainingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserRepositoryFactoryTest {

    @Mock
    private TraineeRepository mockTraineeRepository;
    @Mock
    private TrainerRepository mockTrainerRepository;
    @Mock
    private PartnerRepository mockPartnerRepository;

    private UserRepositoryFactory userRepositoryFactoryUnderTest;

    @BeforeEach
    void setUp() {
        userRepositoryFactoryUnderTest = new UserRepositoryFactory(mockTraineeRepository, mockTrainerRepository,
                mockPartnerRepository);
    }

    @Test
    void testGetUserRepository() {
        // Setup
        // Run the test
        final UserRepository result = userRepositoryFactoryUnderTest.getUserRepository("role");

        // Verify the results
    }
}
