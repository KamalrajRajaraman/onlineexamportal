import React, { useEffect, useState } from "react";
import { BiDetail } from "react-icons/bi";
import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstant";
import { useNavigate } from "react-router-dom";

const PerformanceReport = () => {
    const navigate =useNavigate();
    const [userAttemptList,setUserAttemptList] = useState([]);
  useEffect(() => {
    fetchUserAttemptList();
  }, []);

  const fetchUserAttemptList = async () => {
    const res = await fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        "findAllUserAttemptByPartyId",
      { credentials: "include" }
    );
    const data = await res.json();
    if(data.result==="success"){
        setUserAttemptList(data.userAttemptMasterList);
    }
    console.log(data);
  };
  return (
    <>
      <div className="container-fluid border-bottom border-dark border-3">
        <h2 className="p-2 ">Performance Report</h2>
      </div>
      <div className="container-fluid border">
        
        <table className="table border mt-2 table-striped">
          <thead>
            <tr className="align-content-center ">
              <th scope="col">Attempt Number</th>
              <th scope="col">Performance Id</th>
              <th scope="col">Exam Name</th>
              <th scope="col">Score</th>
              <th scope="col">Completed Date</th>
              <th scope="col">No Of Questions</th>
              <th scope="col">Total Correct</th>
              <th scope="col">Total Wrong</th>
              <th scope="col">User Passed</th>
              <th scope="col">Details</th>
            </tr>
          </thead>
          <tbody>
            {userAttemptList&&userAttemptList.map((record)=><tr key={record.performanceId}>
                <td>{record.attemptNumber}</td>
              <td>{record.performanceId}</td>
              <td>{record.examName}</td>
              <td>{record.score}</td>
              <td>{record.completedDate}</td>
              <td>{record.noOfQuestions}</td>
              <td>{record.totalCorrect}</td>
              <td>{record.totalWrong}</td>
              <td>{record.userPassed}</td>
              <td><BiDetail onClick={()=>{navigate(`view-details/${record.performanceId}`,{state:record})}} /></td>
            </tr>)}

          </tbody>
        </table>
      </div>
    </>
  );
};

export default PerformanceReport;
