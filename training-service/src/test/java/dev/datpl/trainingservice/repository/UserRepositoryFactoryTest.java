package dev.datpl.trainingservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    void getUserRepository_TraineeRole_ReturnsTraineeRepository() {
        UserRepository result = userRepositoryFactoryUnderTest.getUserRepository("trainee");
        assertEquals(mockTraineeRepository, result);
    }

    @Test
    void getUserRepository_TrainerRole_ReturnsTrainerRepository() {
        UserRepository result = userRepositoryFactoryUnderTest.getUserRepository("trainer");
        assertEquals(mockTrainerRepository, result);
    }

    @Test
    void getUserRepository_PartnerRole_ReturnsPartnerRepository() {
        UserRepository result = userRepositoryFactoryUnderTest.getUserRepository("partner");
        assertEquals(mockPartnerRepository, result);
    }

    @Test
    void getUserRepository_InvalidRole_ReturnsNull() {
        UserRepository result = userRepositoryFactoryUnderTest.getUserRepository("invalidRole");
        assertNull(result);
    }

}
