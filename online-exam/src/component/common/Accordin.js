import { useState } from "react";

const Accordin = ({ object ,name ,onDelete,id,onEdit,showDetails}) => {
  
  const [toggle, setToggle] = useState(true);
  const onClick = () => {
    setToggle(!toggle);
  };


 
  return (
    <div className=" bg-white border">
      <div className="accordion accordion-flush">
        <div className="accordion-item">
          <h2 className="accordion-header ">
            <div
              onClick={onClick}
              className={`accordion-button ${ "collapsed"}`}
              type="button"
            >
              <h5 className="col-10">{object[name]}</h5>
              <p></p>
              <div className=" ms-1 row    ">
               {showDetails && <button onClick={()=>showDetails(object[id], object)} className="btn  btn-outline-primary  btn-sm me-2 col">Details</button>}
              <button   onClick={()=>onEdit(object[id], object)} className="btn  btn-outline-success  btn-sm me-2 col">Edit</button>
              <button onClick={()=>onDelete(object[id])} className="btn btn-outline-danger  btn-sm me-2 col ">Delete</button>
            </div>
            </div>
           
          </h2>
          <div className={`accordion-collapse ${ "collapse"}`}>
            <div className="accordion-body">
            
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Accordin;
