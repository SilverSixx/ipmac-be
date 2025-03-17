package dev.datpl.trainingservice.controller;

import dev.datpl.trainingservice.pojo.request.AssignTraineeToPartnerDto;
import dev.datpl.trainingservice.service.ITraineeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private ITraineeService traineeService;

    @InjectMocks
    private TrainingController trainingController;

    @Test
    void healthCheck_ReturnsOk() {
        ResponseEntity<?> response = trainingController.healthCheck();
        assertEquals(ResponseEntity.ok("Training service is running"), response);
    }

    @Test
    void assignPartner_ValidInput_ReturnsOk() {
        AssignTraineeToPartnerDto dto = new AssignTraineeToPartnerDto();
        dto.setPartnerId("partnerId");
        doNothing().when(traineeService).assignTraineeToPartner(anyString(), anyString());

        ResponseEntity<?> response = trainingController.assignPartner("userId", dto);

        assertEquals(ResponseEntity.ok("Trainee assigned to partner"), response);
        verify(traineeService).assignTraineeToPartner("userId", "partnerId");
    }

    @Test
    void assignPartner_InvalidInput_ThrowsException() {
        AssignTraineeToPartnerDto dto = new AssignTraineeToPartnerDto();
        dto.setPartnerId(null);

        trainingController.assignPartner("userId", dto);
    }
}
