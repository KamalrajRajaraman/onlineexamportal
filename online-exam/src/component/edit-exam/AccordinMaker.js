import React, { useEffect } from "react";
import EditExamAccordin from "./EditEXamAccordin";

const AccordinMaker = ({ objects ,id ,name ,onDelete,onEdit,questions}) => {

  return (
    <>
    <h4 className="mt-2 bg-secondary text-light p-2">Topics</h4>
    <div className="mt-2">
      {objects.map((object) => (
        <EditExamAccordin key={object[id]} object={object} name={name} onDelete={onDelete} onEdit ={onEdit} id ={id}  topics={questions}/>
      ))}
    </div>
    </>
  );
};

export default AccordinMaker;
