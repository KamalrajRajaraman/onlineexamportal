import React from "react";
import { Link } from "react-router-dom";

const SideBar = () => {
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
      </div>
    </>
  );
};

export default SideBar;
