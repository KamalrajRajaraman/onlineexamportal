import React, { useEffect, useState } from "react";
import MainContent from "./MainContent";
import AccordinMaker from "./AccordinMaker";
import { useQuestionContext } from "./questionData";


const Questions = () => {
  const {questions, setQuestions,onDelete} = useQuestionContext();
  useEffect(()=>{
    fetchQuestion();
    return()=>{
      setQuestions([])
    }
  },[])

  const fetchQuestion =async()=>{
    const res =await fetch("https://localhost:8443/onlineexam/control/findAllQuestions");
    const data = await res.json();
    const questionList = data.questionList
   setQuestions([...questions,...questionList])
    
  }

  const text ={
    header:"Questions",
    btnText:"Questions"
   }

  
  return (
    <>
      <MainContent text={text} to="add-questions" back="/admin/questions" />
      <AccordinMaker objects={questions}  id={"questionId"} name={"questionDetail"} onDelete={onDelete}/>
    </>
  );
};

export default Questions;
