import React, { useState } from "react";
import { useNavigate, Outlet } from "react-router-dom";

const MainContent = ({ text, to, back }) => {
  const [show, setShow] = useState(false);
  const navigate = useNavigate();
  const onClick = () => {
    show ? navigate(back) : navigate(to);
    setShow(!show);
  };
  return (
    <>
      <div className="main-content  container-fluid  border-bottom border-3 border-dark    ">
        <div className="row">
          <div className="col-8 p-0">
            <h2 className="p-2">{text}</h2>
          </div>
          <div className="col-4">
            <div className="d-grid gap-2 d-md-flex justify-content-md-end m-2 me-4">
              <button
                onClick={onClick}
                className={`btn btn-lg m-1 ${
                  show ? "btn-danger" : " btn-primary"
                }`}
                type="button"
              >
                {show ? "Close" : `Add ${text}`}
              </button>
            </div>
          </div>
        </div>
      </div>
      <Outlet />
    </>
  );
};

export default MainContent;
