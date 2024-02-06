package com.ssafy.lam.admin.service;

import com.ssafy.lam.freeboard.domain.Freeboard;
import com.ssafy.lam.freeboard.domain.FreeboardRepository;
import com.ssafy.lam.freeboard.dto.FreeboardAdminDto;
import com.ssafy.lam.hospital.domain.HospitalRepository;
import com.ssafy.lam.hospital.dto.HospitalAdminDto;
import com.ssafy.lam.reviewBoard.domain.ReviewBoardRepository;
import com.ssafy.lam.reviewBoard.dto.ReviewBoardAdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final FreeboardRepository freeboardRepository;
    private final ReviewBoardRepository reviewBoardRepository;
    private final HospitalRepository hospitalRepository;

    @Override
    public List<FreeboardAdminDto> findComplainedAndNotDeletedFreeboards() {

        return freeboardRepository.findByComplainTrueAndIsDeletedFalse()
                .stream()
                .map(freeboard -> FreeboardAdminDto.builder()
                        .freeboardSeq(freeboard.getFreeboardSeq())
                        .userId(freeboard.getUser().getUserId())
                        .userName(freeboard.getUser().getName())
                        .freeboardTitle(freeboard.getTitle())
                        .freeboardContent(freeboard.getContent())
                        .complain(freeboard.isComplain())
                        .isDeleted(freeboard.isDeleted())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewBoardAdminDto> findComplainedAndNotDeletedReviewBoards() {
        return reviewBoardRepository.findByComplainTrueAndIsdeletedFalse()
                .stream()
                .map(reviewBoard -> ReviewBoardAdminDto.builder()
                        .reviewBoard_seq(reviewBoard.getSeq())
                        .reviewBoard_title(reviewBoard.getTitle())
                        .reviewBoard_content(reviewBoard.getContent())
                        .customer_name(reviewBoard.getUser().getName())
                        .reviewBoard_doctor(reviewBoard.getDoctor())
                        .reviewBoard_region(reviewBoard.getRegion())
                        .score(reviewBoard.getScore())
                        .reviewBoard_hospital(reviewBoard.getHospital())
                        .reviewBoard_price(reviewBoard.getPrice())
                        .regdate(reviewBoard.getRegdate())
                        .complain(reviewBoard.isComplain())
                        .isdeleted(reviewBoard.isIsdeleted())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalAdminDto> findUnapprovedHospitals() {
        return hospitalRepository.findByIsApprovedFalse().stream()
                .map(hospital -> HospitalAdminDto.builder()
                        .hospitalSeq(hospital.getHospitalSeq())
                        .userSeq(hospital.getUser().getUserSeq())
                        .hospitalInfo_id(hospital.getUser().getUserId())
                        .hospitalInfo_name(hospital.getUser().getName())
                        .isApproved(hospital.isApproved())
                        .build())
                .collect(Collectors.toList());
    }

}