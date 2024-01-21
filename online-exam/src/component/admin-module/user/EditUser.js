import React, { createContext, useState } from 'react'
import MainContent from "../../common/MainContent"

import { useNavigate, useParams } from 'react-router-dom';

import ShowExam from './ShowExam';
import UserDetails from './UserDetails';
import { POST,CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION, alert, swalFireAlert } from "../../common/CommonConstants";

export const EditUserContext=createContext();

const EditUser = () => {

  const navigate = useNavigate();
  const { partyId } = useParams();
  const [exams, setExams] = useState([]);
  const fetchAllExamsForUser =()=>{
    fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`findAllExamForPartyId`,
    {...POST,body: JSON.stringify({ partyId }),})
      .then((response) => {
        if (response.status === 401) {
          navigate("/login");
        }
        return response.json();
      })
      .then((data) => {
        if (data.result==="error"){
          swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
        }
        setExams(data.examList);
        console.log(data.examList);
      });
  }
 

  const text ={
    header:"User Exam Mapping",
    btnText:"exam to user"
   }
   
  return (
   <EditUserContext.Provider value={{partyId,fetchAllExamsForUser}}>
      <UserDetails />
      <MainContent  text={text} to="add-exam-to-user" back={`/admin/user/edit/userId/${partyId}`}/>
      {exams?<ShowExam  exams ={exams} fetchAllExamsForUser={fetchAllExamsForUser}/>:"No Exams added to this user"}
   </EditUserContext.Provider>
  )
}

export default EditUser