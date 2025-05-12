package org.govwatcher.model.materized;

import com.querydsl.core.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Table(name = "politician_detail_mat")
@Getter
@NoArgsConstructor
public class VPoliticianDetail {

    @Id
    @Column(name = "politician_id")
    private Long id;

    @Column(name = "mona_cd")
    private String monaCD;

    @Column(name = "name")
    private String name;

    @Column(name = "eng_name")
    private String engName;

    @Column(name = "hanja_name")
    private String hanjaName;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "gender")
    private String gender;

    @Column(name = "unit")
    private int unit;

    @Column(name = "district")
    private String district;

    @Column(name = "position")
    private String position;

    @Column(name = "party")
    private String party;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "office_room")
    private String officeRoom;

    @Column(name = "staff")
    private String staff;

    @Column(name = "secretary")
    private String secretary;

    @Column(name = "secretary2")
    private String secretary2;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "total_bills")
    private Long totalBills;

    @Column(name = "recent_bills")
    private Long recentBills;

    @Column(name = "passed_bills")
    private Long passedBills;
}