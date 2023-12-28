import React from 'react'

const Alert = ({close,message,color}) => {
  return (
    <div className= {`alert ${color} mt-2 z-3 col-8 top-0 end-0 position-absolute `} role="alert">
    <div className="row ">
      <div className="col-10">
        <h4 className="alert-heading">Well done!</h4>
      </div>
      <div className="col-2 d-grid gap-2 d-md-flex justify-content-md-end">   
        <button
          type="button"
          className="btn-close "
          aria-label="Close"
          onClick={()=>close(false)}
        ></button>
      </div>
    </div>
    <p>
        {message}
    </p>
   
  </div>
  )
}

export default Alert