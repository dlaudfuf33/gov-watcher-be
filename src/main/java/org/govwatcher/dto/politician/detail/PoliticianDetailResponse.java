package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;
import org.govwatcher.model.materized.VPoliticianDetail;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class PoliticianDetailResponse {
    private ProfileDto profile;
    private ContactDto contact;
    private SnsDto sns;

    private String career;
    private List<PoliticianTermDto> terms;

    private BillActivityDto billActivities;

    public static PoliticianDetailResponse of(
            VPoliticianDetail mat,
            String career,
            List<PoliticianTermDto> terms,
            List<SimpleBillDto> bills
    ) {
        double passRate = (mat.getTotalBills() != null && mat.getTotalBills() > 0)
                ? Math.round((mat.getPassedBills() * 1000.0 / mat.getTotalBills())) / 10.0
                : 0.0;

        return PoliticianDetailResponse.builder()
                .profile(ProfileDto.builder()
                        .id(mat.getId())
                        .monaCD(mat.getMonaCD())
                        .name(mat.getName())
                        .engName(mat.getEngName())
                        .hanjaName(mat.getHanjaName())
                        .birthDate(mat.getBirthDate())
                        .gender(mat.getGender())
                        .party(mat.getParty())
                        .district(mat.getDistrict())
                        .term(mat.getUnit())
                        .profileImage(mat.getProfileImage())
                        .build())
                .contact(ContactDto.builder()
                        .phone(mat.getPhone())
                        .email(mat.getEmail())
                        .homepage(mat.getHomepage())
                        .officeRoom(mat.getOfficeRoom())
                        .aides(ContactDto.Aides.builder()
                                .staff(safeSplit(mat.getStaff()))
                                .secretary(safeSplit(mat.getSecretary()))
                                .secretary2(safeSplit(mat.getSecretary2()))
                                .build())
                        .build())
                .sns(SnsDto.builder()
                        .facebook(mat.getFacebookUrl())
                        .twitter(mat.getTwitterUrl())
                        .youtube(mat.getYoutubeUrl())
                        .build())
                .career(career)
                .terms(terms)
                .billActivities(BillActivityDto.builder()
                        .totalBills(mat.getTotalBills())
                        .recentBills(mat.getRecentBills())
                        .passRate(passRate)
                        .bills(bills)
                        .build())
                .build();
    }

    private static List<String> safeSplit(String input) {
        return (input == null || input.isBlank())
                ? List.of()
                : Arrays.stream(input.split("\\s*,\\s*")).toList();
    }
}