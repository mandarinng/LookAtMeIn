package com.ssafy.lam.freeboard.service;

import com.ssafy.lam.comment.domain.Comment;
import com.ssafy.lam.comment.dto.CommentRequestDto;
import com.ssafy.lam.comment.service.CommentService;
import com.ssafy.lam.common.EncodeFile;
import com.ssafy.lam.config.MultipartConfig;
import com.ssafy.lam.customer.domain.Customer;
import com.ssafy.lam.customer.domain.CustomerRepository;
import com.ssafy.lam.exception.NoArticleExeption;
import com.ssafy.lam.file.domain.UploadFile;
import com.ssafy.lam.file.service.UploadFileService;
import com.ssafy.lam.freeboard.domain.Freeboard;
import com.ssafy.lam.freeboard.domain.FreeboardRepository;
import com.ssafy.lam.freeboard.dto.FreeboardRequestDto;
import com.ssafy.lam.freeboard.dto.FreeboardResponseDto;
import com.ssafy.lam.hashtag.service.FreeboardHashtagService;
import com.ssafy.lam.user.domain.User;
import com.ssafy.lam.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreeboardServiceImpl implements FreeboardService {
    private final FreeboardRepository freeboardRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final CommentService commentService;
    private final UploadFileService uploadFileService;
    private final FreeboardHashtagService freeboardHashtagService;

    private Logger log = LoggerFactory.getLogger(FreeboardServiceImpl.class);
    MultipartConfig multipartConfig = new MultipartConfig();
    private String uploadPath = multipartConfig.multipartConfigElement().getLocation();

    @Override
    public Freeboard createFreeboard(FreeboardRequestDto freeboardRequestDto) {
        User user = userRepository.findById(freeboardRequestDto.getUser_seq()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        // 파일 저장
        // DTO의 MultiPartFile을 받아서 서비스로 보내서 파일 저장함
        UploadFile uploadFile = null;
        if (freeboardRequestDto.getUploadFile() != null)
            uploadFile = uploadFileService.store(freeboardRequestDto.getUploadFile());

        log.info("글 등록 유저 정보: {}", user);

        // 게시글에 UploadFile을 연관지어주고 저장
        Freeboard freeboard = Freeboard.builder()
                .user(user)
                .uploadFile(uploadFile)
                .title(freeboardRequestDto.getFreeBoard_title())
                .content(freeboardRequestDto.getFreeBoard_content())
                .build();

        Freeboard savedFreeboard =freeboardRepository.save(freeboard);

//        freeboardHashtagService.saveHashtag(savedFreeboard,freeboardRequestDto.getTagNames());

        return savedFreeboard;
    }

    @Override
    public List<Freeboard> getAllFreeboards() {
        List<Freeboard> freeboardList = freeboardRepository.findByIsDeletedFalse();
        return freeboardList;
    }

    @Override
    public FreeboardResponseDto getFreeboard(Long freeBoardSeq) {
        // 상세조회하려는 게시글 가져오기
        Freeboard freeboard = freeboardRepository.findByfreeboardSeqAndIsDeletedFalse(freeBoardSeq).orElseThrow(() -> new NoArticleExeption("해당 게시글이 없습니다."));

        // 게시글 작성자 정보 가져오기
        Customer customer = customerRepository.findByUserUserSeq(freeboard.getUser().getUserSeq()).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));


        FreeboardResponseDto freeboardResponseDto = FreeboardResponseDto.builder()
                .freeboardSeq(freeboard.getFreeboardSeq())
                .userId(freeboard.getUser().getUserId())
                .freeboardTitle(freeboard.getTitle())
                .freeboardContent(freeboard.getContent())
                .freeboardRegisterdate(freeboard.getRegisterDate())
                .userEmail(customer.getEmail())
                .build();

        if(customer.getProfile() != null){
            UploadFile customerProfile = customer.getProfile();
            Path path = Paths.get(uploadPath+"/"+customerProfile.getName());
            try{
                String customerProfileBase64 = EncodeFile.encodeFileToBase64(path);
                String customerProfileType = customerProfile.getType();
                freeboardResponseDto.setCustomerProfileBase64(customerProfileBase64);
                freeboardResponseDto.setCustomerProfileType(customerProfileType);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }


        if (freeboard.getUploadFile() != null) {
            try {
                UploadFile uploadFile = uploadFileService.getUploadFile(freeboard.getUploadFile().getSeq());
                Path path = Paths.get(uploadPath + "/" + uploadFile.getName());
                String base64 = EncodeFile.encodeFileToBase64(path);
                String type = uploadFile.getType();
                freeboardResponseDto.setBase64(base64);
                freeboardResponseDto.setType(type);
                freeboardResponseDto.setFileSeq(uploadFile.getSeq());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        // 현재 게시물에 달린 댓글 가져오기
        List<Comment> commentList = commentService.getAllComments(freeBoardSeq);
        List<CommentRequestDto> commentRequestDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
//            System.out.println("comment = " + comment.getContent());
            CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                    .comment_seq(comment.getSeq())
                    .comment_content(comment.getContent())
                    .customer_name(comment.getUserId())
                    .customerId(comment.getUserId())
                    .freeboard_seq(comment.getFreeboard().getFreeboardSeq())
                    .regdate(comment.getRegdate())
                    .build();
            commentRequestDtoList.add(commentRequestDto);
        }

        freeboardResponseDto.setComments(commentRequestDtoList);

        return freeboardResponseDto;
    }

    @Override
    public Freeboard updateFreeboard(Long freeboardSeq, FreeboardRequestDto updateFreeboardRequestDto) {
        Freeboard freeboard = freeboardRepository.findById(freeboardSeq)
                .orElseThrow(() -> new NoArticleExeption("해당 게시글이 없습니다."));

        if (!freeboard.getUser().getUserSeq().equals(updateFreeboardRequestDto.getUser_seq())) {
            throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");
        }

        Freeboard updatedFreeboard = Freeboard.builder()
                .freeboardSeq(freeboard.getFreeboardSeq())
                .title(updateFreeboardRequestDto.getFreeBoard_title())
                .content(updateFreeboardRequestDto.getFreeBoard_content())
                .build();


        return freeboardRepository.save(updatedFreeboard);
    }


    @Override
    public Freeboard deleteFreeboard(Long freeBoardSeq) throws NoArticleExeption {

        Freeboard freeboard = freeboardRepository.findById(freeBoardSeq).orElseThrow(() -> new NoArticleExeption("해당 게시글이 없습니다."));
        List<Comment> commentList = commentService.getAllComments(freeBoardSeq);


        log.info("게시글 삭제 전 정보: {}", freeboard);
        freeboard.setDeleted(true);
        freeboardRepository.save(freeboard);
        for (Comment comment : commentList) {
            commentService.deleteComment(comment.getSeq());
        }

        log.info("게시글 삭제 후 정보: {}", freeboard);


        return freeboard;

    }

    @Override
    public List<FreeboardResponseDto> getFreeboardByUserSeq(Long userSeq) {
        // userSeq를 사용하여 삭제되지 않은 게시글 목록을 조회합니다.
        List<Freeboard> freeboards = freeboardRepository.findByUserUserSeqAndIsDeletedFalse(userSeq);
        Customer customer = customerRepository.findByUserUserSeq(userSeq).get();

        // 조회된 Freeboard 엔티티 리스트를 FreeboardResponseDto 리스트로 변환합니다.
        return freeboards.stream().map(freeboard -> {
            // 각 Freeboard 엔티티를 FreeboardResponseDto로 매핑합니다.
            // 여기서는 CommentRequestDto 리스트를 빈 리스트로 설정합니다. 필요하다면, 실제 댓글 데이터를 조회하여 설정할 수 있습니다.
            return FreeboardResponseDto.builder()
                    .freeboardSeq(freeboard.getFreeboardSeq())
                    .userId(freeboard.getUser().getUserId())
                    .userEmail(customer.getEmail())
                    .freeboardTitle(freeboard.getTitle())
                    .freeboardCnt(freeboard.getCnt())
                    .freeboardRegisterdate(freeboard.getRegisterDate())
                    .freeboardContent(freeboard.getContent())
                    .comments(new ArrayList<>()) // 실제 댓글 데이터가 필요한 경우 여기를 수정
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public boolean reportFreeboard(Long freeBoardSeq) {
        Optional<Freeboard> reportFreeBoard = freeboardRepository.findById(freeBoardSeq);
        if(reportFreeBoard.isPresent()) {
            Freeboard selectedFreeBoard = reportFreeBoard.get();
            selectedFreeBoard.setReport(true);
            freeboardRepository.save(selectedFreeBoard);
            return true;
        }
        else {
            return false;
        }
    }

}
