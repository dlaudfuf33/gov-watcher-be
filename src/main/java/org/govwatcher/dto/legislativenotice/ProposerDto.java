package org.govwatcher.dto.legislativenotice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProposerDto {
    private long id;
    private String name;
    private String monaCD;
    private String party;
    private String imageUrl;
    private String role;
}