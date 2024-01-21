import React from 'react'
import {  NavLink } from 'react-router-dom'
import { useAuth } from '../../auth/Auth';
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../../common/CommonConstants";
const UserSideBar = () => {
  const {logout} = useAuth();
  const onlogout =async() =>{
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"onlineExamLogout",{credentials:"include"});
    const data = await res.json();
    console.log(data);
    if(data.result==="success"){
      logout();
    }
  }
  return (
    <div>
      <>
      <div className="list-group">
       
        <NavLink to="exams" className="list-group-item list-group-item-action">
          Exams
        </NavLink>
        <NavLink to="performance-report" className="list-group-item list-group-item-action">
         Performance Report
        </NavLink>

        <button  onClick = {onlogout}type="button" className="list-group-item list-group-item-action" >Logout</button>
      

      </div>
    </>
    </div>
  )
}

export default UserSideBar















