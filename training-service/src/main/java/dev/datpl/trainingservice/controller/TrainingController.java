package dev.datpl.trainingservice.controller;

import dev.datpl.trainingservice.pojo.request.AssignTraineeToPartnerDto;
import dev.datpl.trainingservice.service.ITraineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/training")
public class TrainingController {

    private final ITraineeService traineeService;

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Training service is running");
    }

    @PostMapping("/trainees/{userId}/assign-partner")
    public ResponseEntity<?> assignPartner(@PathVariable String userId, @Validated @RequestBody AssignTraineeToPartnerDto dto) {
        traineeService.assignTraineeToPartner(userId, dto.getPartnerId());
        return ResponseEntity.ok("Trainee assigned to partner");
    }

}
