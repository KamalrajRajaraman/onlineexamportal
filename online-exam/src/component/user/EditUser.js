import React, { createContext } from 'react'
import MainContent from '../common/MainContent'
import { useParams } from 'react-router-dom';
export const EditUserContext=createContext();
const EditUser = () => {
  const { partyId } = useParams();

  const text ={
    header:"User Exam Mapping",
    btnText:"exam to user"
   }
   
  return (
   <EditUserContext.Provider value={{partyId}}>
    <div className="row">
        <div className="col p-0 border-bottom border-3 border-dark">
          <h2 className="p-2">User Details</h2>
        </div>
      </div>
   <MainContent text={text} to="add-exam-to-user" back={`/admin/user/edit/userId/${partyId}`} />

   </EditUserContext.Provider>
  )
}

export default EditUser