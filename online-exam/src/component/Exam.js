import React, { useEffect, useState } from "react";
import MainContent from "./MainContent";
import AccordinMaker from "./AccordinMaker";
import { Outlet, useNavigate } from "react-router-dom";
import { useData } from "./data";

const Exam = () => {
  const store =useData();
  const [exams, setExams] = useState([
    {
      examId: "1",
      description: "Java",
    },
    {
      examId: "2",
      description: "Css",
    },
  ]);

  useEffect(()=>{
    const examList =store.getData();
    setExams([...exams,...examList]) 
  },[]);

  const getExams =()=>{
    console.log("getExams Called")

  }
  return (
    <>
      <MainContent text={"Exam"} to="add-exam" back="/admin/exam" />
      <AccordinMaker objects={exams} id={"examId"} name={"description"} />
    </>
  );
};

export default Exam;
