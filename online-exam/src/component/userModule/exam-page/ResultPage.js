import React, { useEffect, useState } from "react";
import useStateRef from "react-usestateref";

import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstant";

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
      <table className="table table-striped border">
        <thead>
          <tr>
            <th scope="col">Exam Name</th>
            <th scope="col">Total Correct</th>
            <th scope="col">Total Wrong</th>
            <th scope="col">Total Question</th>
            <th scope="col">Score</th>
            <th scope="col">Result</th>
            
          </tr>
        </thead>
        <tbody>
          <tr>
            <th scope="row">Java</th>
            <td> {result.totalCorrect}</td>
            <td> {result.totalWrong}</td>
            <td> {result.noOfQuestions}</td>
            <td> {result.score}</td>
            <td>{result.userPassed}</td>
          </tr>
         
        </tbody>
      </table>
      <div className="mt-3">
        <h4>Topicwise Results</h4>
      </div>
      <table class="table table-striped border ">
      <thead>
         
        <tr >
          <th scope="col">TopicId</th>
          <th scope="col">Topic pass percentage</th>
          <th scope="col">User topic percentage</th>
          <th scope="col">User passed this topic</th>
        </tr>
        </thead>
        <tbody>
        {topicResult &&
          topicResult.map((value) => {
            return (
              <tr className="mt-4" key={value.topicId}>
                <td>{value.topicId}</td>
                <td>{value.topicPassPercentage}</td>
                <td>{value.userTopicPercentage}</td>
                <td>{value.userPassedThisTopic}</td>
              </tr>
            );
          })}</tbody>
      </table>
     

    </div>
  );
};

export default ResultPage;
