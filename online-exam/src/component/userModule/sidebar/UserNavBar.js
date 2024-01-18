import React from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/Auth";
import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
} from "../../common/CommonConstant";

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
      { credentials: "include" }
    );
    const data = await res.json();
    console.log(data);
    if (data.result === "success") {
      logout();
    }
  };
  return (
    <div className="row ">
      <nav>
        <div class="nav nav-tabs" id="nav-tab" role="tablist">
          <button
          onClick={()=>{navigate("exams")}}
            class="nav-link active"
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
            class="nav-link"
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
            class="nav-link "
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