package dev.datpl.trainingservice.pojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignTraineeToPartnerDto {
    @NotBlank(message = "Partner ID is required")
    String partnerId;
}
