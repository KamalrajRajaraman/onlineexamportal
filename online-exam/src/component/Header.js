import React, { useState } from "react";

const Header = () => {
  const [toggle, setToggle] = useState(false);
  const onClick = () => {
    setToggle(!toggle);
  };
  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark ">
      <div className="container-fluid">
        <a className="navbar-brand  fw-bold" href="#">
          VastPro
        </a>
        <button
          onClick={onClick}
          className={`navbar-toggler ${toggle || "collapsed"} `}
          type="button"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div
          className={`collapse navbar-collapse ${toggle && "show"}`}
          id="navbarNav"
        ></div>
      </div>
    </nav>
  );
};

export default Header;
