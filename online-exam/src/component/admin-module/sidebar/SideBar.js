import React from "react";
import { Link, NavLink } from "react-router-dom";
import { useAuth } from "../../auth/Auth";

const SideBar = () => {
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
    <>
      <div className="list-group">
        <Link
          to="#"
          className="list-group-item list-group-item-action  bg-secondary text-white"
        >
          Admin
        </Link>
        <NavLink to="exam" className="list-group-item list-group-item-action">
          Exam
        </NavLink>
        <NavLink to="topic" className="list-group-item list-group-item-action">
          Topic
        </NavLink>
        <NavLink to="questions" className="list-group-item list-group-item-action">
          Questions
        </NavLink>
        <NavLink to="user" className="list-group-item list-group-item-action">
          Add User
        </NavLink>
        <button  onClick = {onlogout}type="button" className="list-group-item list-group-item-action" >Logout</button>
      </div>
    </>
  );
};

export default SideBar;
