package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ContactDto {
    private String phone;
    private String email;
    private String homepage;
    private String officeRoom;
    private Aides aides;

    @Getter
    @Builder
    public static class Aides {
        private List<String> staff;
        private List<String> secretary;
        private List<String> secretary2;
    }
}