import React, { useEffect } from "react";
import Accordin from "./Accordin";

const AccordinMaker = ({ objects ,id ,name  }) => {
  useEffect(()=>{

  },[objects])
  return (
    <div className="mt-2">
      {objects.map((object) => (
        <Accordin key={object[id]} object={object} name={name} />
      ))}
    </div>
  );
};

export default AccordinMaker;
