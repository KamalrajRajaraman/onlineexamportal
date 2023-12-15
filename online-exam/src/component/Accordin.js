import { useState } from "react";

const Accordin = ({ object ,name ,onDelete,id,onEdit}) => {
  
  const [toggle, setToggle] = useState(true);
  const onClick = () => {
    setToggle(!toggle);
  };

 
  return (
    <div className=" bg-white border">
      <div className="accordion accordion-flush">
        <div className="accordion-item">
          <h2 className="accordion-header position-relative ">
            <button
              onClick={onClick}
              className={`accordion-button ${toggle && "collapsed"}`}
              type="button"
            >
              <h5>{object[name]}</h5>
              <p></p>
            </button>
            <div className="  row z-3 position-absolute end-0 me-5  top-0 mt-3 ">
              <button   onClick={()=>onEdit(object[id])} className="btn btn-outline-success  btn-sm me-4 col">Edit</button>
              <button onClick={()=>onDelete(object[id])} className="btn btn-outline-danger btn-sm me-3 col ">Delete</button>
            </div>
          </h2>
          <div className={`accordion-collapse ${toggle && "collapse"}`}>
            <div className="accordion-body">
              Placeholder content for this accordion, which is intended to
              demonstrate the <code>.accordion-flush</code> class. This is the
              first item's accordion body.
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Accordin;
