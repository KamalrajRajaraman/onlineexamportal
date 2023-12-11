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
  const redirectPath = location.state?.path || "/admin";

  const onLogin = async (userDetails) => {
    console.log(userDetails);

    const res = await fetch(
      `https://localhost:8443/onlineexam/control/onlineExamLogin`,
      {
        method: "POST",
        headers: {
          "Content-type": "application/json",
        },
        body: JSON.stringify(userDetails),
      }
    );

    const data = await res.json();
    if (data._LOGIN_PASSED_ === "TRUE") {
      auth.login(
        data.USERNAME,
        data["org.apache.tomcat.util.net.secure_requested_ciphers"]
      );

      navigate(redirectPath, { replace: true });
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

  return (
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
  );
};

export default Login;
