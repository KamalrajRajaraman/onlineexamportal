import React, { useState } from "react";
import Input from "./Input";

const Addtopic = () => {
  const [topicId, settopicId] = useState("");
  const [topicName, setTopicName] = useState("");
  return (
    <div className="container-fluid border my-2 fw-bold ">
      <div className="col-4 mx-auto mt-2 ">
        <Input
          name={"topicId"}
          text="Topic Id"
          state={topicId}
          setStateFun={settopicId}
          type={"text"}
          placeholder={""}
        />
      </div>
      <div className="col-4 mx-auto">
        <Input
          name={"topicName"}
          text="topicName"
          state={topicName}
          setStateFun={setTopicName}
          type={"text"}
          placeholder={""}
        />
      </div>

      <div className="d-grid gap-2 col-2 mx-auto mb-2 mb-2">
        <button className="btn btn-primary" type="submit">
          Submit
        </button>
      </div>
    </div>
  );
};

export default Addtopic;
