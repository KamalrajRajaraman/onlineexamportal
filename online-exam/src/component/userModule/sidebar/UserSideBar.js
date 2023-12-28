import React from 'react'
import {  NavLink } from 'react-router-dom'
import { useAuth } from '../../auth/auth';
const UserSideBar = () => {
  const {logout} = useAuth();
  const onlogout =async() =>{
    const res = await fetch("https://localhost:8443/onlineexam/control/onlineExamLogout",{credentials:"include"});
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

        <button  onClick = {onlogout}type="button" class="list-group-item list-group-item-action" >Logout</button>
      

      </div>
    </>
    </div>
  )
}

export default UserSideBar















