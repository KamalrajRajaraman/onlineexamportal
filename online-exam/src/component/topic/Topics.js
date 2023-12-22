import React, { useEffect, useState } from "react";
import MainContent from "../common/MainContent";
import AccordinMaker from "../common/AccordinMaker";
import { useTopicContext } from "./topicData";
import Alert from "../common/Alert";

const Topics = () => {
  const { topics, setTopics,fetchTopic,onDelete,alert,setAlert } = useTopicContext();
  
  useEffect(() => {
    getTopics();
    return () => {
      setTopics([]);
    };
  }, []);

  const getTopics = async () => {
    const topicList = await fetchTopic();
    setTopics(topicList);
  };
  const text ={
    header:"Topic",
    btnText:"Topic"
   }
  // const fetchTopic = async () => {
  //   const topicList = await getTopics();
  //   setTopics([...topics, ...topicList]);
  // };

  // const getTopics = async () => {
  //   const res = await fetch(
  //     "https://localhost:8443/onlineexam/control/findAllTopics"
  //   );
  //   const data = await res.json();
  
  //   const { topicList } = data;
  //   return topicList;
  // };

  return (
    <div className="position-relative">
     {alert &&<Alert color={"alert-success"} close={setAlert} message ={"Aww yeah, you successfully Topic Exam"}/>}
      <MainContent text={text} to="add-topic" back="/admin/topic" />
      <AccordinMaker objects={topics} id={"topicId"} name={"topicName"} onDelete={onDelete} />
    </div>
  );
};

export default Topics;
