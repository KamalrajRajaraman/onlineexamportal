import React from "react";
import Accordin from "./Accordin";

const AccordinMaker = ({ objects ,id ,name ,onDelete,onEdit, path}) => {

  return (
    <div className="mt-2">
      {objects.map((object) => (
        <Accordin key={object[id]} object={object} name={name} onDelete={onDelete} onEdit ={onEdit} id ={id} path={path}/>
      ))}
    </div>
  );
};

export default AccordinMaker;
