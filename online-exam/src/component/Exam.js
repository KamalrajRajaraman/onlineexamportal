import React, { useEffect, useState } from "react";
import MainContent from "./MainContent";
import AccordinMaker from "./AccordinMaker";
import { useExamContext } from "./examData";
import { useNavigate } from "react-router-dom";


const Exam = () => {
  const navigate =useNavigate();
  const {exams,setExams,onDelete} = useExamContext();

  useEffect(()=>{   
    fetchExam();
    return ()=>{
      setExams([]);
    }

  },[]);

  const fetchExam = async()=>{
    const result = await getExams();
    const examList= result.examList;
     setExams([...exams,...examList])
  }
  

  const getExams =async()=>{
    const res= await fetch("https://localhost:8443/onlineexam/control/findAllExams");
    const data = await res.json();
    return data;
 }

 const onEdit =(id)=>{
  navigate(`edit/examId/${id}`);

 }
 const text ={
  header:"Exam",
  btnText:"Exam"
 }
 
  return (
    <>
      <MainContent text={text} to="add-exam" back="/admin/exam" />
      <AccordinMaker objects={exams} id={"examId"} name={"examName"} onDelete={onDelete} onEdit={onEdit}/>
    </>
  );
};

export default Exam;
