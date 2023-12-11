import React, { useState } from "react";
import MainContent from "./MainContent";
import AccordinMaker from "./AccordinMaker";


const Questions = () => {
  const [questions, setQuestions] = useState([
    {
      id: "1",
      name: "What is encapsulation ?",
    },
    {
      id: "2",
      name: "What is method overloading ?",
    },
  ]);
  return (
    <>
      <MainContent text={"Questions"} to="add-questions" back="/admin/questions" />
      <AccordinMaker objects={questions}  id={"id"} name={"name"}/>
    </>
  );
};

export default Questions;
