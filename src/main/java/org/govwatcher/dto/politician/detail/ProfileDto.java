package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileDto {
    private Long id;
    private String name;
    private String monaCD;
    private String engName;
    private String hanjaName;
    private String birthDate;
    private String gender;
    private String party;
    private String district;
    private int term;
    private String profileImage;
}