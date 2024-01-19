import React, { useEffect, useState } from "react";
import useStateRef from "react-usestateref";

import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstant";
import UserAttemptDetailsTable from "../user-dashboard/UserAttemptDetailsTable";

const ResultPage = () => {
  const [result, setResult] = useState({
    noOfQuestions: "",
    score: "",
    totalCorrect: "",
    totalWrong: "",
    userPassed: "",
    passPercentage: "",
    actualUserPercentage: "",
  });
  const [topicResult, setTopicResult, topicResultRef] = useStateRef([]);

  useEffect(() => {
    fetchResult();
  }, []);

  const fetchResult = async () => {
    try {
      const res = await fetch(
        PROTOCOL +
          DOMAIN_NAME +
          PORT_NO +
          WEB_APPLICATION +
          CONTROL_SERVLET +
          "evaluateUserAttemptAnswerMaster",
        {
          credentials: "include",
        }
      );
      const data = await res.json();
      const resultDetails = data.resultMap;

      setTopicResult(
        ...topicResult,
        resultDetails.updatedUserAttemptTopicMasterList
      );

      const resultStatus = resultDetails.userPassed === "Y" ? "Pass" : "Fail";
      setResult({
        examName: resultDetails.examName,
        noOfQuestions: resultDetails.noOfQuestions,
        score: resultDetails.score,
        totalCorrect: resultDetails.totalCorrectQuestionsInExam,
        totalWrong: resultDetails.totalWrongAnswersInExam,
        passPercentage: resultDetails.passPercentage,
        actualUserPercentage: resultDetails.actualUserPercentage,
        userPassed: resultStatus,
      });
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className="container-fluid border mt-2">
      <div>
        <h2>Exam Results</h2>
      </div>

      <UserAttemptDetailsTable
        userAttemptRecord={result}
        topicResult={topicResult}
      />
    </div>
  );
};

export default ResultPage;
