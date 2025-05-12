package org.govwatcher.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.govwatcher.model.Politician;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DemographicStatsResponse {
    private DataContainer data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer {
        private String[] labels = {"30대 이하", "40대", "50대", "60대 이상"};
        private int[] male;
        private int[] female;
    }

    public static DemographicStatsResponse fromEntity(List<PoliticianSimpleDTO> dtos) {
        int[] male = new int[4];
        int[] female = new int[4];

        for (PoliticianSimpleDTO dto : dtos) {
            if (dto.getBirthDate() == null || dto.getGender() == null) {
                continue;
            }

            int age = calculateAge(dto.getBirthDate());
            int ageGroup = getAgeGroup(age);
            if (ageGroup == -1) {
                continue;
            }

            if (dto.getGender() == Politician.Gender.MALE) {
                male[ageGroup]++;
            } else if (dto.getGender() == Politician.Gender.FEMALE) {
                female[ageGroup]++;
            }
        }

        return new DemographicStatsResponse(
                new DataContainer(
                        new String[]{"30대 이하", "40대", "50대", "60대 이상"},
                        male,
                        female
                )
        );
    }

    // 나이 계산
    private static int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // 나이 그룹 결정
    private static int getAgeGroup(int age) {
        if (age <= 39) return 0;
        else if (age <= 49) return 1;
        else if (age <= 59) return 2;
        else if (age >= 60) return 3;
        else return -1;
    }
}
