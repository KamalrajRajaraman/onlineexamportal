import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ExamAccordion from "./ExamAccordion";
import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstant";
const ExamsForUser = () => {
  const [exams, setExams] = useState([]);

  useEffect(() => {
    fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        "findAllExamForPartyId",
      {
        credentials: "include",
      }
    )
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        console.log("data.examlist::::", data.examList);
        setExams(data.examList);
      });
  }, []);

  return (
    <div className="container-fluid ">
      <div className="container-wells border-bottom border-dark border-3">
        <h2 className="p-2 ">Scheduled Exams</h2>
      </div>
      <div className="mt-1 ">
        {exams.map((exam) => (
          <ExamAccordion key={exam.examId} object={exam} />
        ))}
      </div>
    </div>
  );
};

export default ExamsForUser;
