import React, { createContext } from 'react'
import MainContent from '../common/MainContent'
import { Outlet, useNavigate, useParams } from 'react-router-dom';
export const EditUserContext=createContext();
const EditUser = () => {
  const { partyId } = useParams();
  const navigate = useNavigate();

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
      <nav>
        <div className="nav nav-tabs mt-2 border navbar-light bg-light fw-bold   px-2 pt-2" id="nav-tab" role="tablist">
        
          <button
            className="nav-link  text-dark active"
            id="nav-home-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-home"
            type="button"
            role="tab"
           
            onClick={()=>navigate("view-all-exam")}
          >
            
           User Exam Details
          </button>
          <button
            className="nav-link  text-dark "
            id="nav-profile-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-profile"
            type="button"
            role="tab"
            onClick={()=>navigate("add-exam-to-user")}
          >
            Add Exam to User
          </button>

        
          
        </div>
      </nav>
      <Outlet/>

   </EditUserContext.Provider>
  )
}

export default EditUser