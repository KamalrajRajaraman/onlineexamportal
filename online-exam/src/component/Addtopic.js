import React, { useState } from "react";
import Input from "./Input";
import { useTopicContext } from "./topicData";

const Addtopic = () => {
  const{topics,setTopics} =useTopicContext();
  const [topicId, settopicId] = useState("");
  const [topicName, setTopicName] = useState("");

  const onCreateTopic =async (topicDetails)=>{
    const res = await fetch("https://localhost:8443/onlineexam/control/createTopic",{
      method:"POST",
      headers:{
        'Content-type':"application/json"
      },
      body:JSON.stringify(topicDetails)
    });

    const {topic} = await res.json();
    setTopics([...topics,topic]);
    
  

  }

  const onSubmit =(e)=>{

    e.preventDefault();
    onCreateTopic({topicId,topicName});
    settopicId("");
    setTopicName("");

  }
  return (
    <div className="container-fluid border my-2 fw-bold ">
       <form onSubmit={onSubmit}>
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
      </form>
    </div>
  );
};

export default Addtopic;
