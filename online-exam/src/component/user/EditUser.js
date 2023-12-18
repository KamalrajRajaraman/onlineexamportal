import React from 'react'
import MainContent from '../common/MainContent'
import { useParams } from 'react-router-dom';

const EditUser = () => {
  const { partyId } = useParams();

  const text ={
    header:"User Exam Mapping",
    btnText:"exam to user"
   }
   
  return (
   <>
   <MainContent text={text} to="add-exam-to-user" back={`/admin/user/edit/userId/${partyId}`} />
   </>
  )
}

export default EditUser