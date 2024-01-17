import React, { useState } from "react";
import Input from "../common/Input";
import { useAuth } from "./Auth";
import { useLocation, useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import { CONTROL_SERVLET, DOMAIN_NAME, PORT_NO,  PROTOCOL, WEB_APPLICATION } from "../common/CommonConstant"
const Login = () => {
  const auth = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState("");
  const [pass, setPass] = useState("");
  

  const onLogin = async (userDetails) => {
    console.log(userDetails);

    const res = await fetch(PROTOCOL +DOMAIN_NAME+PORT_NO+WEB_APPLICATION+CONTROL_SERVLET+
      "onlineExamLogin",
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
         
        },
        credentials: 'include',
        body: JSON.stringify(userDetails),
      }
    );

    const data = await res.json();

    if (data._LOGIN_PASSED_ === "TRUE") {
      auth.login(data.USERNAME,data.partyRoleTypeId);

      if(data.partyRoleTypeId==="PERSON_ROLE"){
        const redirectPath = location.state?.path || "/user-dashboard";

        if(redirectPath.startsWith("/user-dashboard")){
          navigate(redirectPath, { replace: true });
        }
        else{
          navigate("/error/user-credentials");
        }
      }

      if(data.partyRoleTypeId==="ADMIN"){
        const redirectPath = location.state?.path || "/admin";
        if(redirectPath.startsWith("/admin")){
             navigate(redirectPath, { replace: true });
        }
        else{
          navigate("/error/admin-credentials");
        }
      }
     
     
    }
    if (data._ERROR_MESSAGE_) {
      Swal.fire({
        title:"Error",
        text:data._ERROR_MESSAGE_,
        icon: "error"
      });
      
    }

    console.log(data);
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (!user) {
      Swal.fire({
        title:"Missing credential",
        text:"Please enter user name...",
        icon: "question"
      });
      
      return;
    }
    if (!pass) {
      Swal.fire({
        title:"Missing credential",
        text:"please enter password...",
        icon: "question"
      });
      return;
    }

    let USERNAME = user;
    let PASSWORD = pass;

    onLogin({ USERNAME, PASSWORD });

    setUser("");
    setPass("");
  };

  return (<>
    <div className="container">
      <div className="row mt-5 align-items-center justify-content-center">
        <div className="col-5 mt-5  rounded-2 shadow-lg">
              <h4 className="text-center  fw-bold   p-3 border-bottom ">Login</h4>

          <form className="p-2" onSubmit={onSubmit}>
            <Input
              name={"user"}
              text="User"
              state={user}
              setStateFun={setUser}
              type={"text"}
              placeholder={""}
            />
            <Input
              name={"InputPassword"}
              text="Password"
              state={pass}
              setStateFun={setPass}
              type={"password"}
              placeholder={""}
              passView={true}
            />
            <button type="submit" className="btn btn-primary">
              Login
            </button>
          </form>
        </div>
      </div>
    </div>
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark  p-3 " style={{marginTop:137}}>
      <p className='float-end ' ></p>
      
    </nav>
    </>
  );
};

export default Login;
