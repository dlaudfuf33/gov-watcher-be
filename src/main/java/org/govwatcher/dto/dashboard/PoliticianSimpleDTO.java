package org.govwatcher.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.govwatcher.model.Politician;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoliticianSimpleDTO {
    private Long id;
    private LocalDate birthDate;
    private Politician.Gender gender;
}