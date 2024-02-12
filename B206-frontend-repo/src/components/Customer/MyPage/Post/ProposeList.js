import React, { useState, useEffect } from "react";
import { Routes, Route, NavLink } from "react-router-dom";
import axiosApi from "../../../../api/axiosApi";
import { useSelector } from "react-redux";
import styles from "./ProposeList.module.css";

function ProposeList() {
  const [proposeList, setProposeList] = useState([]);
  const userSeq = useSelector((state) => state.user.userSeq);

  useEffect(() => {
    axiosApi
      .get(`api/mypage/notifications/${userSeq}`)
      .then((response) => {
        console.log(response.data);
        setProposeList(response.data);
      })
      .catch((error) => {
        console.error(
          "마이페이지에서 나에게 상담을 제안한 목록 조회 에러 : ",
          error
        );
      });
  }, []);

  function startChat() {}

  return (
    <>
      <h3>여기는 요청받은 채팅 목록</h3>
      <div className={styles.container}>
        {proposeList.map((propose, index) => (
          <li key={index} className={styles.proposeItem}>
            <div className={styles.index}>No. {index + 1}</div>
            <div className={styles.writer}>
              <div>{propose.hospitalName}</div>
            </div>
            <div>
              <div>{propose.message}</div>
            </div>
            <div>
              <button onClick={startChat}>수락하기</button>
            </div>
          </li>
        ))}
      </div>
    </>
  );
}
export default ProposeList;
