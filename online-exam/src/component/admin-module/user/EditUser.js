import React, { createContext, useState } from 'react'
import MainContent from "../../common/MainContent"

import { useParams } from 'react-router-dom';

import ShowExam from './ShowExam';
import UserDetails from './UserDetails';
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO, PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstant";
export const EditUserContext=createContext();
const EditUser = () => {
  const { partyId } = useParams();
  const [exams, setExams] = useState([]);
  const fetchAllExamsForUser =()=>{
    fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+`findAllExamForPartyId`, {
      method: "POST",
      headers: { "Content-type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ partyId }),
    })
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        console.log("final data:::", data);
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
      <ShowExam  exams ={exams} fetchAllExamsForUser={fetchAllExamsForUser}/>

   </EditUserContext.Provider>
  )
}

export default EditUser