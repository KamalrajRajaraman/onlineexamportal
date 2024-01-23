import React, { useEffect, useReducer, useState } from "react";
import Input from "../common/Input";
import { useAuth } from "./Auth";
import { useLocation, useNavigate } from "react-router-dom";

import {
  CONTROL_SERVLET,
  DOMAIN_NAME,
  PORT_NO,
  PROTOCOL,
  WEB_APPLICATION,
  POST, swalFireAlert
} from "../common/CommonConstants";

const initialState = {
  loading: false,
  error: "",
  data: {},
};

const reducer = (state, action) => {
  switch (action.type) {
    case "FETCHING":
      return { loading: true, error: "", data: {} };
    case "FETCH_SUCCESS":
      return { loading: false, error: "", data: action.payload };
    case "FETCH_ERROR":
      return { loading: false, error: action.error, data: {} };
    default:
      return initialState;
  }
};

const Login = () => {
  const [fetchedData, dispatch] = useReducer(reducer, initialState);
  
  const auth = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [user, setUser] = useState("");
  const [pass, setPass] = useState("");

  const onLogin = (userDetails) => {
    console.log(userDetails);
    dispatch({ type: "FETCHING" });
    fetch(
      PROTOCOL +
        DOMAIN_NAME +
        PORT_NO +
        WEB_APPLICATION +
        CONTROL_SERVLET +
        "onlineExamLogin",
      { ...POST, body: JSON.stringify(userDetails) }
    )
      .then((res) => res.json())
      .then((data) => {
        dispatch({ type: "FETCH_SUCCESS", payload: data });
      })
      .catch((e) => {
        dispatch({ type: "FETCH_ERROR", error: e.message });
      });
  };

  useEffect(() => {
   
    FetchSucess();
  }, [fetchedData]);

  const FetchSucess = () => {
    const { _LOGIN_PASSED_, USERNAME, partyRoleTypeId, _ERROR_MESSAGE_ } =
      fetchedData.data;
    if (_LOGIN_PASSED_ === "TRUE") {
      
      auth.login(USERNAME, partyRoleTypeId);

      if (partyRoleTypeId === "PERSON_ROLE") {
        const redirectPath = location.state?.path || "/user-dashboard";

        if (redirectPath.startsWith("/user-dashboard")) {
          navigate(redirectPath, { replace: true });
        } else {
          navigate("/error/user-credentials");
        }
      }

      if (partyRoleTypeId === "ADMIN") {
        const redirectPath = location.state?.path || "/admin";
        if (redirectPath.startsWith("/admin")) {
          navigate(redirectPath, { replace: true });
        } else {
          navigate("/error/admin-credentials");
        }
      }
    }
    if (_ERROR_MESSAGE_) {
      swalFireAlert("Error",_ERROR_MESSAGE_, "error");
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();

    if (!user) {
      swalFireAlert("Missing credential","Please enter user name...","question");
      return;
    }
    if (!pass) {
      swalFireAlert("Missing credential","please enter password...","question");
      return;
    }

    let USERNAME = user;
    let PASSWORD = pass;

    onLogin({ USERNAME, PASSWORD });

    setUser("");
    setPass("");
  };

  

  return (
    <>{fetchedData.loading ?
    <div class="d-flex justify-content-center" style={{marginTop:220}}>
    <div class="spinner-border text-primary" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
      
  :<>
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
      <nav
        className="navbar navbar-expand-lg navbar-dark bg-dark  p-3 "
        style={{ marginTop: 78 }}
      >
        <p className="float-end "></p>
      </nav></>}
    </>
  );
};

export default Login;
