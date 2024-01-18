import React, { createContext } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import "./styles/User.css"

export const UserContext = createContext(null);
function User() {
  const navigate = useNavigate();

  return (
    <>
      <div className="row">
        <div className="col p-0 border-bottom border-3 border-dark">
          <h2 className="p-2">User</h2>
        </div>
      </div>
      <nav className="">
        <div
          className="nav bg-secondary nav-tabs mt-2 border  fw-bold   px-2 pt-2"
          id="nav-tab"
          role="tablist"
        >
          <button
            className="nav-link text-dark active"
            id="nav-home-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-home"
            type="button"
            role="tab"
            aria-controls="nav-home"
            aria-selected="true"
            onClick={() => navigate("list-user")}
          >
            List User
          </button>
          <button
            className="nav-link  text-dark "
            id="nav-profile-tab"
            data-bs-toggle="tab"
            data-bs-target="#nav-profile"
            type="button"
            role="tab"
            aria-controls="nav-profile"
            aria-selected="false"
            onClick={() => navigate("add-user")}
          >
            Add User
          </button>
        </div>
      </nav>
      <Outlet />
    </>
  );
}

export default User;
