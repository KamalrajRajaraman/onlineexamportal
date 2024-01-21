import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/Auth";
import "./styles/UserNavBar.css"
import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  GET,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstants";

const UserNavBar = () => {
   const navigate = useNavigate();
  const { logout } = useAuth();
  const onlogout = async () => {
    const res = await fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        "onlineExamLogout",
     GET
    );
    const data = await res.json();
    console.log(data);
    if (data.result === "success") {
      logout();
    }
  };
  return (
    <div className="">
      <nav  className="">
        <div class="nav nav-tabs custom-user-nav container-fluid fw-bold" id="nav-tab" role="tablist">
         <button
          onClick={()=>{navigate("exams")}}
            class="nav-link custom-nav-text active"
            id="nav-home-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-home"
            type="button"
            role="tab"
            aria-controls="nav-home"
            aria-selected="true"
          >
           Exams
          </button>
          <button
            onClick={()=>{navigate("performance-report")}}
            class="nav-link custom-nav-text"
            id="nav-profile-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-profile"
            type="button"
            role="tab"
            aria-controls="nav-profile"
            aria-selected="false"
          >
             Performance Report
          </button>

          <button
            onClick={onlogout}
            class="btn-logout ms-auto  fw-bold"
            id="nav-contact-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-contact"
            type="button"
            role="tab"
            aria-controls="nav-contact"
            aria-selected="false"
          >
            Logout
          </button>
        </div>
         
       
          
        
      
      </nav>
     
    </div>
  );
};

export default UserNavBar;
