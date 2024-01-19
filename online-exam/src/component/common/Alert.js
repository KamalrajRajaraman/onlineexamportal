import React from "react";

const Alert = ({alertInfo}) => {
  return (
    <>
      <button
        id="alert-button"
        type="button"
        className="btn btn-primary d-none"
        data-bs-toggle="modal"
        data-bs-target="#Modal"
      >
        Launch demo modal
      </button>

      <div
        className="modal fade "
        id="Modal"
        tabIndex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog  modal-dialog-centered col">
          <div className={`alert col alert-dismissible fade show ${alertInfo.color}`} role="alert">
            <h4 className="alert-heading">{alertInfo.header}</h4>
            <p>
              {alertInfo.content}
            </p>
            
            <button id ="close-alert" type="button" className="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </div>
      </div>

    
    </>
  );
};

export default Alert;
