import React, { useEffect, useState } from "react";

const Alert = ({alertInfo}) => {


  const [time,setTime] =useState(0)
  const [seconds,setSeconds] = useState(0);
  
  

  return (
    <>
      <button
        id="alert-button"
        type="button"
        class="btn btn-primary d-none"
        data-bs-toggle="modal"
        data-bs-target="#Modal"
      >
        Launch demo modal
      </button>

      <div
        class="modal fade "
        id="Modal"
        tabindex="-1"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div class="modal-dialog  modal-dialog-centered col">
          <div class={`alert col alert-dismissible fade show ${alertInfo.color}`} role="alert">
            <h4 class="alert-heading">{alertInfo.header}</h4>
            <p>
              {alertInfo.content}
            </p>
            
            <button id ="close-alert" type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </div>
      </div>

    
    </>
  );
};

export default Alert;
