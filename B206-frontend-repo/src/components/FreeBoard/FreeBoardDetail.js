import React, { useEffect, useState } from "react";
import axiosApi from "../../api/axiosApi";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import FreeBoardDelete from "./FreeBoardDelete";
import FreeBoardUpdate from "./FreeBoardUpdate";
import Comment from "../Comment/Comment";

function FreeBoardDetail() {
  const [post, setPost] = useState(null);
  const { freeboardSeq } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPost = async () => {
      try {
        let response = await axiosApi.get(
          `/api/freeBoard/freeBoardList/${freeboardSeq}`
        );

        response = await axiosApi.get(
          `/api/file/${response.data.freeboardImg}`
        )

        // setPost(response.data);
        console.log(response.data);
      } catch (error) {
        console.log("자유게시판 상세 불러오기 실패 : ", error);
      }
    };
    if (freeboardSeq) {
      fetchPost();
    }
  }, [freeboardSeq]);
  //에러찾을라고..
  if (!post) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <h3>자유 게시판 상세</h3>
      <div>작성자 아이디: {post.userId}</div>
      <div>작성자 이메일: {post.userEmail}</div>
      <div>작성 날짜: {post.freeboardRegisterdate}</div>
      <img src={
        
        post.freeboardImg
      } alt="게시글 이미지" />
      <div>글 내용: {post.freeboardContent}</div>
      <div>글 제목: {post.freeboardTitle}</div>
      {/* <div>해시태그: {post.hashTag}</div> */}
      <FreeBoardDelete freeBoardSeq={freeboardSeq} />
      <FreeBoardUpdate
        freeboardContent={post.freeboardContent}
        freeboardTitle={post.freeboardTitle}
        freeboardSeq={post.freeboardSeq}
      />

      <Comment comments={post.comments} freeboardSeq={post.freeboardSeq} />
    </>
  );
}
export default FreeBoardDetail;
//뜬다!!
