import React, { useState } from "react";
import Input from "./Input";
import { useAuth } from "../auth";
import { useLocation, useNavigate } from "react-router-dom";

const Login = () => {
  const auth = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState("");
  const [pass, setPass] = useState("");
  

  const onLogin = async (userDetails) => {
    console.log(userDetails);

    const res = await fetch(
      `https://localhost:8443/onlineexam/control/onlineExamLogin`,
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
      auth.login(data.USERNAME,data.partyRole);

      if(data.partyRole==="PERSON_ROLE"){
        const redirectPath = location.state?.path || "/user-dashboard";

        if(redirectPath.startsWith("/user-dashboard")){
          navigate(redirectPath, { replace: true });
        }
        else{
          navigate("/error/user-credentials");
        }
      }

      if(data.partyRole==="ADMIN"){
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
      alert(data._ERROR_MESSAGE_);
    }

    console.log(data);
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (!user) {
      alert("please enter user name");
      return;
    }
    if (!pass) {
      alert("please enter password");
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
