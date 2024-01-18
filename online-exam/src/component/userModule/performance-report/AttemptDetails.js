import React, { useEffect, useState } from "react";
import UserAttemptDetailsTable from "../user-dashboard/UserAttemptDetailsTable";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { CONTROL_SERVLET, DOMAIN_NAME, GET, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";

const AttemptDetails = () => {
    const {performanceId} =useParams();
  const userAttemptRecord = useLocation().state;
  const [userAttemptTopicMasterList,setUserAttemptTopicMasterList] =useState();

  useEffect(()=>{
    fetchUserAttemptTopicMaster();
  },[])

  const fetchUserAttemptTopicMaster=async()=>{
    const res = await fetch(PROTOCOL+DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`findUserAttemptTopicMasterByPerforamceId?performanceId=${performanceId}`,GET);
    const data = await res.json();
    if(data.result==="success"){

        setUserAttemptTopicMasterList(data.userAttemptTopicMasterList);
    }
  }
  return (
    <>
      <div className="container-fluid border-bottom border-dark border-3">
        <h2 className="p-2 ">Attempt Details</h2>
      </div>
      <UserAttemptDetailsTable userAttemptRecord={userAttemptRecord} topicResult={userAttemptTopicMasterList}/>
    </>
  );
};

export default AttemptDetails;
