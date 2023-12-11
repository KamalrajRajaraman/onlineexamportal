import React, { useState } from "react";
import MainContent from "./MainContent";
import AccordinMaker from "./AccordinMaker";


const Topics = () => {
  const [topics, setTopics] = useState([
    {
      id: "1",
      name: "Oops",
    },
    {
      id: "2",
      name: "Methods",
    },
  ]);
  return (
    <>
      <MainContent text={"Topic"} to="add-topic" back="/admin/topic" />
      <AccordinMaker objects={topics} id={"id"}  name={"name"}/>
    </>
  );
};

export default Topics;
