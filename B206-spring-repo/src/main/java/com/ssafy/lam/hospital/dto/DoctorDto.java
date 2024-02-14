package com.ssafy.lam.hospital.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DoctorDto {

    private Long doc_info_seq;
    private String doc_info_name;
    private List<CategoryDto> doc_info_category;
    private List<CareerDto> doc_info_career;
    private double doc_info_avgScore;
    private int doc_info_cntReviews;

    private String doctorProfileBase64; // 의사 프로필 이미지
    private String doctorProfileType; // 의사 프로필 이미지 타입


    @Builder
    public DoctorDto(Long doc_info_seq, String doc_info_name, List<CategoryDto> doc_info_category, List<CareerDto> doc_info_career, double doc_info_avgScore, int doc_info_cntReviews, String doctorProfileBase64, String doctorProfileType) {
        this.doc_info_seq = doc_info_seq;
        this.doc_info_name = doc_info_name;
        this.doc_info_category = doc_info_category;
        this.doc_info_career = doc_info_career;
        this.doc_info_avgScore = doc_info_avgScore;
        this.doc_info_cntReviews = doc_info_cntReviews;
        this.doctorProfileBase64 = doctorProfileBase64;
        this.doctorProfileType = doctorProfileType;
    }
}
