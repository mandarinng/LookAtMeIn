package com.ssafy.lam.reviewBoard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReviewListDisplay {

    private String customer_name; // 작성자
    private String reviewBoard_title;// 제목
    private int reviewBoard_cnt; // 조회수
    private long reviewBoard_regDate; // 작성날짜
    private double reviewBoard_score; // 별점
    private String reviewBoard_doctor; // 시술 의사
    private String reviewBoard_region; // 지역
    private String reviewBoard_surgery; // 시술 부위
    private String reviewBoard_hospital; // 병원 이름
    private int reviewBoard_price; // 시술 가격

    @Builder
    public ReviewListDisplay(String customer_name, String reviewBoard_title, int reviewBoard_cnt,
                             long reviewBoard_regDate, double reviewBoard_score, String reviewBoard_doctor,
                             String reviewBoard_region, String reviewBoard_surgery, String reviewBoard_hospital,
                             int reviewBoard_price) {
        this.customer_name = customer_name;
        this.reviewBoard_title = reviewBoard_title;
        this.reviewBoard_cnt = reviewBoard_cnt;
        this.reviewBoard_regDate = reviewBoard_regDate;
        this.reviewBoard_score = reviewBoard_score;
        this.reviewBoard_doctor = reviewBoard_doctor;
        this.reviewBoard_region = reviewBoard_region;
        this.reviewBoard_surgery = reviewBoard_surgery;
        this.reviewBoard_hospital = reviewBoard_hospital;
        this.reviewBoard_price = reviewBoard_price;
    }
}
