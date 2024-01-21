import React from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/Auth";
import { CONTROL_SERVLET, DOMAIN_NAME, GET, PORT_NO,  PROTOCOL, WEB_APPLICATION, swalFireAlert } from "../../common/CommonConstants";

const SideBar = () => {

  const navigate =useNavigate();
  const {logout} = useAuth();
  const onlogout =async() =>{
    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+"onlineExamLogout",GET);
    if (res.status === 401) {
      navigate("/login");
    }
    const data = await res.json();
    
    if(data.result==="success"){
      logout();
     
    } else if (data.result==="error"){
      swalFireAlert(  "Error",data._ERROR_MESSAGE_, "error");
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
