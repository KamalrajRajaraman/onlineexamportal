import React from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../auth";

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
        <Link to="exam" className="list-group-item list-group-item-action">
          Exam
        </Link>
        <Link to="topic" className="list-group-item list-group-item-action">
          Topic
        </Link>
        <Link to="questions" className="list-group-item list-group-item-action">
          Questions
        </Link>
        <Link to="user" className="list-group-item list-group-item-action">
          Add User
        </Link>
        <button  onClick = {onlogout}type="button" className="list-group-item list-group-item-action" >Logout</button>
      </div>
    </>
  );
};

export default SideBar;
